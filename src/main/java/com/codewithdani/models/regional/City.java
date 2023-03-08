package com.codewithdani.models.regional;

import com.codewithdani.models.actions.Measure;
import com.codewithdani.models.data.Data;
import com.codewithdani.models.data.InfectionData;

import java.util.Random;

import static com.codewithdani.models.actions.government.Restriction.CONTACT_RESTRICTIONS_VALUE;
import static com.codewithdani.models.actions.self.Distancing.SOCIAL_DISTANCING_VALUE;

public class City {
    private final String name;
    private final int populationDensity;
    private int population;
    private double obedience = 1;
    private double contactRestrictions = 0;
    private int contactRestrictionsDaysLeft;
    private transient InfectionData infectionData;

    // min and max amount a persons meets other persons per day (random)
    private static final double MIN_AMOUNT_OF_MEETINGS_PER_DAY = 1.9;
    private static final double MAX_AMOUNT_OF_MEETINGS_PER_DAY = 2.0;

    // min and max amount of infected people for the first day (random)
    private static final int MIN_FIRST_DAY_INFECTED_PEOPLE = 5;
    private static final int MAX_FIRST_DAY_INFECTED_PEOPLE = 5;

    // min and max obedience
    private static final double MIN_OBEDIENCE = 0.10;
    private static final double MAX_OBEDIENCE = 1;


    public City(String name, int population, int populationDensity) {
        this.name = name;
        this.population = population;
        this.populationDensity = populationDensity;
        this.infectionData = new InfectionData(this, population);
    }

    public String getName() {
        return name;
    }

    public int calculateNextDayInfections(double stateInfectionRatio, Data data, Measure measure){
        Random random = new Random();
        int lowestCityDensity = data.getLowestCityDensity();
        double populationDensityModifier = 0.15; // + Boost for the largest city and - brake for the smallest city
        double protectionAfterFirstInfection = 0.1; // 10% more safety if you had the virus 1 time

        double densityOffset = 0; // offset 0 = no offset, 0.1 moves the bounds of the highest and smallest city
        double densityWeight = 1; // increases the importance of this factor

        int newVirusBoostBoundary = 48;

        // first day
        if (infectionData.isNewVirus() && infectionData.getSevenDaysIncidence() < newVirusBoostBoundary){
            infectionData.setNewVirus(false);

            int infections = (MIN_FIRST_DAY_INFECTED_PEOPLE + (MAX_FIRST_DAY_INFECTED_PEOPLE - MIN_FIRST_DAY_INFECTED_PEOPLE) * random.nextInt());
            this.getInfectionData().setFirstNewCases(infections);
            this.getInfectionData().setTotalNewCases(infections);

            return infections;
        }

        // probability between 0% and 20% depending on the density of the city (min/max: city with lowest/highest density)
        double normalizedDensity = (this.populationDensity - lowestCityDensity) / (double)data.getDifferenceBetweenHighestAndLowestDensity();// Densities Cottbus and München (min/max)
        double populationDensityProbability =  (((normalizedDensity * 2) - 1) * populationDensityModifier * densityWeight) + (1 + densityOffset); // 4790 Density (München) equals factor of 20% = 0.2

        // probability depending on the proportion of healed or vaccinated cases to the total population
        // every person who had the infection at least 1 time is 10% more protected
        // example: (10.000 / 100000) * 0.1 + (1 - 0.1) => 0.99 (1% less probability of infection)
        double decreasingProbabilityGrowingRateOfCuredCases = (((this.getInfectionData().getPopulationLeftFirstInfection() / (double)population) * protectionAfterFirstInfection) + (1 - protectionAfterFirstInfection));

        int infectingCasesFromHealedHistory = this.getInfectionData().getHealedHistory().calculateProbabilityOfAnotherInfection();
        double activeOrHealedFactor = ((double)(this.population - this.getInfectionData().getTotalActiveCases() - this.infectionData.getHealedHistory().getAmountOfHealedCases() + infectingCasesFromHealedHistory) / (double)this.population);
        if (activeOrHealedFactor < 0) activeOrHealedFactor = 0;

        // average amount of People a person meets every day
        double amountOfAveragePeopleMeetings = (MIN_AMOUNT_OF_MEETINGS_PER_DAY + (MAX_AMOUNT_OF_MEETINGS_PER_DAY - MIN_AMOUNT_OF_MEETINGS_PER_DAY) * random.nextDouble()) * activeOrHealedFactor;
        amountOfAveragePeopleMeetings *= (1 / (Math.pow(2, this.contactRestrictions * this.getObedience())));

        // probability someone in the 7 days history infects someone
        double infectingCasesFromActiveHistory = this.getInfectionData().calculateActiveCasesInfectingSomeone();

        double vaccinationProtection = measure.getVaccination().getVaccinationProtection();

        // multiplier between 0.9 - 1
        double vaccinationProtectionRatio = 1 - (this.getInfectionData().getVaccinationProportion() * vaccinationProtection);

        // calculation of the infections for the current day of the infection
        int totalNewInfections = (int)(infectingCasesFromActiveHistory * amountOfAveragePeopleMeetings * populationDensityProbability * vaccinationProtectionRatio * decreasingProbabilityGrowingRateOfCuredCases);

        double firstInfectionsRatio = (double)this.getInfectionData().getPopulationLeftFirstInfection()   / (double)this.population;

        // new first infections and new second (or third infection)
        int firstInfections      = (int)((totalNewInfections * (firstInfectionsRatio)));
        int secondInfections     = (totalNewInfections - firstInfections);

        // occasionally hotspot outbreak
        if (firstInfections + secondInfections == 0 && infectionData.getSevenDaysIncidence() == 0){
            // add infections depending on the ratio of infectedCases (city) and totalPopulation to the state it belongs to
            // the less the infection ratio the more chance of an outbreak
            int additionalOutBreakTotalInfections = (int)((secondInfections + 1) * (getFactorBetweenStateAndCity(secondInfections, stateInfectionRatio)  / 10) + 1 );
            firstInfections+= additionalOutBreakTotalInfections;
        }

        this.getInfectionData().addPopulationAlreadyHadFirstInfection(firstInfections);
        this.getInfectionData().removePopulationLeftFirstInfection(firstInfections);

        if (firstInfections > this.getInfectionData().getPopulationLeftFirstInfection()) {
            this.getInfectionData().setFirstNewCases(this.getInfectionData().getPopulationLeftFirstInfection());
        }
        else {
            this.getInfectionData().setFirstNewCases(firstInfections);
            this.getInfectionData().setTotalNewCases(secondInfections + firstInfections);
        }

        if (name.equalsIgnoreCase("erfurt") || name.equalsIgnoreCase("berlin")){
            System.out.println("_____________________");
            System.out.println("Stadt: " + name);
            System.out.println("Einwohnerzahl: " + population);
            System.out.println("Erstinfektionen des Tages: " + firstInfections);
            System.out.println("Zweitinfektionen des Tages: " + secondInfections);
            System.out.println("Gesamtinfektionen ausstehend: " + this.getInfectionData().getPopulationLeftFirstInfection());
            System.out.println("Gesamtinfektionen gehabt: " + this.getInfectionData().getPopulationAlreadyHadFirstInfection());
            System.out.println("Aktuell infiziert: " + this.getInfectionData().getTotalActiveCases());
            System.out.println("_____________________");
            System.out.println("density prob: " + populationDensityProbability);
        }

         return secondInfections + firstInfections;
    }

    public double getFactorBetweenStateAndCity(int totalNewInfections, double stateInfectionRatio){
        double ratio;
        double ratioMax = 10;

        if (totalNewInfections == 0){
            ratio = ratioMax;
        } else ratio = Math.min(stateInfectionRatio / this.getInfectionData().getCityInfectionRatio(), ratioMax);
        return Math.pow(ratio, 2) / Math.pow(ratioMax, 2);
    }
    public int getPopulation() {
        return population;
    }

    public int getPopulationDensity() {
        return populationDensity;
    }

    public void removeFromPopulation(int deadCases){
        this.population -= deadCases;
    }

    public double getObedience() {
        return obedience;
    }

    public void setContactRestrictions(double restrictions) {
        this.contactRestrictions = restrictions;
    }

    public double getContactRestrictions() {
        return contactRestrictions;
    }

    public void setContactRestrictionDuration(int amountOfDays){
        this.contactRestrictionsDaysLeft = amountOfDays;
    }

    public int getContactRestrictionsDaysLeft() {
        return contactRestrictionsDaysLeft;
    }

    public void loseObedience(double lost){
        if (this.obedience - lost < MIN_OBEDIENCE){
            this.obedience = MIN_OBEDIENCE;
        }
        else{
            this.obedience -= lost;
        }
    }

    public void gainObedience(double gain){
        if (this.obedience + gain > MAX_OBEDIENCE){
            this.obedience = MAX_OBEDIENCE;
        }
        else {
            this.obedience += gain;
        }
    }

    public void activateContactRestrictions(int amountOfDays){
        this.setContactRestrictions(CONTACT_RESTRICTIONS_VALUE);
        this.setContactRestrictionDuration(amountOfDays);
    }

    public InfectionData getInfectionData() {
        return infectionData;
    }

    public void createInfectionData(){
        this.infectionData = new InfectionData(this, population);
    }

    public void resetCityFromRestrictions(boolean socialDistancingActivated){
        double contactRestrictions = 0;
        if (socialDistancingActivated) contactRestrictions = SOCIAL_DISTANCING_VALUE;

        this.setContactRestrictionDuration(0);
        this.setContactRestrictions(contactRestrictions);
    }

    public void setObedience(double obedience) {
        this.obedience = obedience;
    }
}

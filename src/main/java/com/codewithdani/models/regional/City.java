package com.codewithdani.models.regional;

import com.codewithdani.models.actions.Measure;
import com.codewithdani.models.data.Data;
import com.codewithdani.models.data.InfectionData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Random;

import static com.codewithdani.models.actions.government.Restriction.CONTACT_RESTRICTIONS_VALUE;

public class City {
    private final String name;
    private final int populationDensity;
    private int population;
    private double obedience = 1;
    private double contactRestrictions;
    private int contactRestrictionsDaysLeft;
    private transient InfectionData infectionData;

    // min and max amount a persons meets other persons per day (random)
    private static final double MIN_AMOUNT_OF_MEETINGS_PER_DAY = 2.3;
    private static final double MAX_AMOUNT_OF_MEETINGS_PER_DAY = 2.3;

    // min and max amount of infected people for the first day (random)
    private static final int MIN_FIRST_DAY_INFECTED_PEOPLE = 4;
    private static final int MAX_FIRST_DAY_INFECTED_PEOPLE = 7;

    // min and max obedience
    private static final double MIN_OBEDIENCE = 0.01;
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

    public int calculateNextDayInfections(int day, double stateInfectionRatio, Data data, Random random, Measure measure){
        int lowestCityDensity = data.getLowestCityDensity();
        double populationDensityModifier = 0.2; // + Boost for the largest city and - brake for the smallest city
        double protectionAfterFirstInfection = 0.1; // 10% more safety if you had the virus 1 time

        double densityOffset = 0; // offset 0 = no offset, 0.1 moves the bounds of the highest and smallest city
        double densityWeight = 1; // increases the importance of this measure

        // first day
        if (day == 0){
            return (random.nextInt(MAX_FIRST_DAY_INFECTED_PEOPLE - MIN_FIRST_DAY_INFECTED_PEOPLE) + MIN_FIRST_DAY_INFECTED_PEOPLE);
        }

        // Probability between 0% and 20% depending on the density of the city (min/max: city with lowest/highest density)
        int differenceHighestAndLowestDensity = data.getDifferenceBetweenHighestAndLowestDensity(); // Densities Cottbus and München (min/max)
        double normalizedDensity = (this.populationDensity - lowestCityDensity) / differenceHighestAndLowestDensity;
        double populationDensityProbability =  (((normalizedDensity * 2) - 1) * populationDensityModifier * densityWeight) + (1 + densityOffset); // 4790 Density (München) equals factor of 20% = 0.2

        // Probability depending on the proportion of healed or vaccinated cases to the total population
        // every person who had the infection at least 1 time is 10% more protected
        // Example: (10.000 / 100000) * 0.1 + (1 - 0.1) => 0.99 (1% less probability of infection)
        double decreasingProbabilityGrowingRateOfCuredCases = (((this.getInfectionData().getPopulationLeftFirstInfection() / (double)population) * protectionAfterFirstInfection) + (1 - protectionAfterFirstInfection));

        // Average amount of People a person meets every day
        double amountOfAveragePeopleMeetings = MIN_AMOUNT_OF_MEETINGS_PER_DAY + (MAX_AMOUNT_OF_MEETINGS_PER_DAY - MIN_AMOUNT_OF_MEETINGS_PER_DAY) * random.nextDouble();
        amountOfAveragePeopleMeetings *= (1 / (Math.pow(2, this.contactRestrictions) * this.getObedience()));

        // Probability someone in the 7 days history infects someone
        double infectingCases = this.getInfectionData().calculateActiveCasesInfectingSomeone();

        int amountOfPeopleWithAlreadyOneInfectionThatCouldBeInfectedAgain = this.getInfectionData().getHealedHistory().calculateProbabilityOfAnotherInfection();

        double vaccinationProtection = measure.getVaccination().getVaccinationProtection();

        // TODO Proportion zwischen 0 (0) und 1 (100%)
        // Protection zwischen 0.9 - 1
        double vaccinationProtectionRatio = 1 - (this.getInfectionData().getVaccinationProportion() * vaccinationProtection);

        // TODO newFirstInfections sind mehr als totalNewInfections
        // calculation of the infections for the current day of the infection
        int totalMeetingsWithInfectedPeople = (int)((infectingCases * amountOfAveragePeopleMeetings) * ((double)this.getInfectionData().getPopulationLeftFirstInfection() / (double)this.population));

        int newFirstInfections = (int)(totalMeetingsWithInfectedPeople * populationDensityProbability * vaccinationProtectionRatio);
        this.getInfectionData().addPopulationAlreadyHadFirstInfection(newFirstInfections);
        this.getInfectionData().removePopulationLeftFirstInfection(newFirstInfections);

        int totalNewInfections = newFirstInfections + (int)(amountOfPeopleWithAlreadyOneInfectionThatCouldBeInfectedAgain * vaccinationProtectionRatio * decreasingProbabilityGrowingRateOfCuredCases);

        // add infections depending on the ratio of infectedCases (city) and totalPopulation to the state it belongs to
        // the less the infection ratio the more chance of an outbreak
        int additionalOutBreakTotalInfections = (int)((totalNewInfections + 1) * (getFactorBetweenStateAndCity(totalNewInfections, stateInfectionRatio)  / 10) + 1 );

        if (newFirstInfections  > this.getInfectionData().getPopulationLeftFirstInfection()) {
            this.getInfectionData().setFirstInfectionNewCases(this.getInfectionData().getPopulationLeftFirstInfection());
        }
        else {
            this.getInfectionData().setFirstInfectionNewCases(newFirstInfections);
            this.getInfectionData().setTotalNewCases(totalNewInfections + additionalOutBreakTotalInfections);
        }


        return totalNewInfections + additionalOutBreakTotalInfections;
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

    public void setContactRestrictions(double contactRestrictionsOfMotherState) {
        this.contactRestrictions = contactRestrictionsOfMotherState;
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
}

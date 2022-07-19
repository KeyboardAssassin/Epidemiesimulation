package com.codewithdani.models.regional;

import com.codewithdani.models.histories.HealedHistory;
import com.codewithdani.models.threats.Virus;

import java.util.Arrays;
import java.util.Random;

public class City {
    private final String name;
    private final int populationDensity;
    private int population;
    private int populationLeftFirstInfection;
    private int activeCases;
    private int newCases;
    private int fristInfectionNewCases;
    private int totalNewCases;
    private int healedCases;
    private int deadCases;
    private double sevenDaysIncidence;
    private double rValue;
    private int[] caseHistory;
    private HealedHistory healedHistory;
    private Virus currentVirus;
    private float vaccinationProportion;
    double cityInfectionRatio;
    double obedience;
    double contactRestrictionsOfMotherState;
    int contactRestrictionsDaysLeft;

    // min and max amount a persons meets other persons per day (random)
    private static double minAmountOfMeetingsPerDay = 0.1;
    private static double maxAmountOfMeetingsPerDay = 2.4;

    // min and max amount of infected people for the first day (random)
    private static int firstDayInfectedPeopleMin = 4;
    private static int firstDayInfectedPeopleMax = 7;


    public City(String name, int population, int populationDensity) {
        this.name = name;
        this.population = population;
        this.populationDensity = populationDensity;
        this.sevenDaysIncidence = 0.0;
        this.rValue = 0.0;
        this.caseHistory = new int[]{-1, -1, -1, -1, -1, -1, -1};
        this.populationLeftFirstInfection = population;
        this.currentVirus = new Virus("alpha", 100, 0.009);
        this.healedHistory = new HealedHistory();
        this.vaccinationProportion = 0.0f;
        this.cityInfectionRatio = 0.0;
    }

    public void setObedience(double obedience) {
        this.obedience = obedience;
    }

    public String getName() {
        return name;
    }

    public void setHistoryDay(int day, int value){
        this.caseHistory[day - 1] = value;
    }

    public int getTotalActiveCases(){
        return Arrays.stream(caseHistory).filter(x -> x != -1).sum();
    }

    public void updateActiveCases(){
        this.activeCases = getTotalActiveCases();
    }

    public void updateNewCases() {
        // go through every element backwards and set the first found at member variable newCase
        for (int elementOfHistory = 6; elementOfHistory > 0; elementOfHistory--) {
            if (caseHistory[elementOfHistory] != -1) {
                this.setNewCases(caseHistory[elementOfHistory]);
                return;
            }
        }
    }

    public void updateRValue(){
            if (caseHistory[0] != -1 && caseHistory[1] == -1){
                setRValue(caseHistory[0]);
            }
            // if at least 2 days are filled
            else if (caseHistory[0] != -1 && caseHistory[1] != -1 && caseHistory[1] != 0) {
                setRValue(caseHistory[0] / caseHistory[1]);
            }
            // if no day is filled or the second entry is 0 (cannot be devided by 0)
            else {
                setRValue(0);
            }
            return;
    }

    public void addNewEntryToHistory(int amountOfCases, Country country){
        int amountOfFirstCases = this.getFristInfectionNewCases();

        // fill history if one element is not yet filled
        for (int numberOfEntry = 0; numberOfEntry < caseHistory.length; numberOfEntry++){
            if (caseHistory[numberOfEntry] == -1){
                caseHistory[numberOfEntry] = amountOfCases;

                this.setNewCases(amountOfCases);
                this.updatePopulationLeftToInfect(amountOfFirstCases);

                return;
            }
        }

        // calculate Deaths
        int deadCasesWithOutMedicine = this.calculateDeaths();
        int deadCasesAfterMedicine = this.tryToHealWithMedicine(country, deadCasesWithOutMedicine);
        int healedThroughMedicine = deadCasesWithOutMedicine - deadCasesAfterMedicine;

        // update Deaths & Healed Cases
        this.updateDeadCases(deadCasesAfterMedicine);
        this.updateHealedCases((int)(caseHistory[0] * (1 - currentVirus.getMortalityRate())) + healedThroughMedicine);
        this.removeFromPopulation(deadCasesAfterMedicine);

        // todo smartere Lösung finden
        // move the oldest entry in the case history into the healed history
        healedHistory.addEntry(caseHistory[0]);

        // left shift every element and add new cases at the last element
        caseHistory[0] = caseHistory[1];
        caseHistory[1] = caseHistory[2];
        caseHistory[2] = caseHistory[3];
        caseHistory[3] = caseHistory[4];
        caseHistory[4] = caseHistory[5];
        caseHistory[5] = caseHistory[6];
        caseHistory[6] = amountOfCases;

        this.setNewCases(amountOfCases);
        this.updatePopulationLeftToInfect(amountOfFirstCases);
    }

    public int calculateDeaths(){
        int peopleDying = (int)(caseHistory[0] * currentVirus.getMortalityRate());

        // return calculated deaths
        return peopleDying;
    }

    public int tryToHealWithMedicine(Country country, int peopleDying){
        double deathProbabilityWithMedicine = 1 - country.getMeasure().getMedicine().getEffectivityOfMedicine();
        int amountOfDosesAvailable = country.getMeasure().getMedicine().getMedicineInStock();

            // if enough medicine is available for all dying patients
            if (amountOfDosesAvailable >= peopleDying){
                country.getMeasure().getMedicine().useMedicine(peopleDying);
                peopleDying *= deathProbabilityWithMedicine;
            }
            // just use the available medicine on some patients (not all)
            else {
                int amountOfPeopleGivingMedicine = amountOfDosesAvailable;
                country.getMeasure().getMedicine().useMedicine(amountOfDosesAvailable);
                peopleDying = (int)((peopleDying - amountOfPeopleGivingMedicine) + (amountOfPeopleGivingMedicine * deathProbabilityWithMedicine));
            }
        return peopleDying;
    }

    public int getEntryFromHistory(int day){
        return caseHistory[day - 1];
    }

    public int calculateNextDayInfections(int day, double stateInfectionRatio){
        Random random = new Random();

        int highestCityDensity = 4790; // TODO Methode, welche diese Daten setzt erstellen? Aber nur einmal ausführen (Membervariable)
        int lowestCityDensity = 596;
        double populationDensityModifier = 0.2; // + Boost for the largest city and - brake for the smallest city
        double protectionAfterFirstInfection = 0.1; // 10% more safety if you had the virus 1 time

        double densityOffset = 0; // offset 0 = no offset, 0.1 moves the bounds of the highest and smallest city
        double densityWeight = 1; // increases the importance of this measure

        // first day
        if (day == 0){
            return (random.nextInt(firstDayInfectedPeopleMax - firstDayInfectedPeopleMin) + firstDayInfectedPeopleMin);
        }

        // TODO In Setup Methode auslagern - immer gleiches Ergebnis -> Membervariable
        // Probability between 0% and 20% depending on the density of the city (min/max: city with lowest/highest density)
        int differenceHighestAndLowestDensity = highestCityDensity - lowestCityDensity; // Densities Cottbus and München (min/max)
        double normalizedDensity = (this.populationDensity - lowestCityDensity) / differenceHighestAndLowestDensity;
        double populationDensityProbability =  (((normalizedDensity * 2) - 1) * populationDensityModifier * densityWeight) + (1 + densityOffset); // 4790 Density (München) equals factor of 20% = 0.2

        // Probability depending on the proportion of healed or vaccinated cases to the total population
        // every person who had the infection at least 1 time is 10% more protected
        // Example: (10.000 / 100000) * 0.1 + (1 - 0.1) => 0.99 (1% less probability of infection)
        double decreasingProbabilityGrowingRateOfCuredCases = (((this.populationLeftFirstInfection / (double)population) * protectionAfterFirstInfection) + (1 - protectionAfterFirstInfection));

        // Average amount of People a person meets every day
        double amountOfAveragePeopleMeetings = minAmountOfMeetingsPerDay + (maxAmountOfMeetingsPerDay - minAmountOfMeetingsPerDay) * random.nextDouble();
        amountOfAveragePeopleMeetings *= (1 / (Math.pow(2, this.contactRestrictionsOfMotherState) * this.getObedience()));

        // Probability someone in the 7 days history infects someone
        double infectingCases = calculateActiveCasesInfectingSomeone();

        int amountOfPeopleWithAlreadyOneInfectionThatCouldBeInfectedAgain = this.healedHistory.calculateProbabilityOfAnotherInfection();

        double vaccinationProtection = 0.9;

        // TODO Proportion zwischen 0 (0) und 1 (100%)
        // Protection zwischen 0.9 - 1
        double vaccinationProtectionRatio = 1 - (this.getVaccinationProportion() * vaccinationProtection);

        // TODO Maßnahmen wie Isolation und Kontaktbeschränkungen auf aktive Fälle multiplizieren (einbeziehen)
        // TODO newFirstInfections sind mehr als totalNewInfections

        // calculation of the infections for the current day of the infection
        int totalMeetingsWithInfectedPeople = (int)(infectingCases * amountOfAveragePeopleMeetings);

        int newFirstInfections = (int)(totalMeetingsWithInfectedPeople * populationDensityProbability * vaccinationProtectionRatio);
        int totalNewInfections = (int)(newFirstInfections * decreasingProbabilityGrowingRateOfCuredCases) + (int)(amountOfPeopleWithAlreadyOneInfectionThatCouldBeInfectedAgain * vaccinationProtectionRatio);

        // add infections depending on the ratio of infectedCases (city) and totalPopulation to the state it belongs to
        // the less the infection ratio the more chance of an outbreak
        int additionalOutBreakTotalInfections = (int)((totalNewInfections + 1) * (getFactorBetweenStateAndCity(totalNewInfections, stateInfectionRatio)  / 10) + 1 );

        this.setFirstInfectionNewCases(newFirstInfections);
        this.setTotalNewCases(totalNewInfections + additionalOutBreakTotalInfections);

        if (newFirstInfections > populationLeftFirstInfection) {
            this.setFirstInfectionNewCases(populationLeftFirstInfection);
        }


        return totalNewInfections + additionalOutBreakTotalInfections;
    }

    public double getFactorBetweenStateAndCity(int totalNewInfections, double stateInfectionRatio){
        double ratio;
        double RATIO_MAX = 10;

        if (totalNewInfections == 0){
            ratio = RATIO_MAX;
        } else if (stateInfectionRatio / this.cityInfectionRatio > RATIO_MAX) {
            ratio = RATIO_MAX;
        } else {
            ratio = stateInfectionRatio / this.cityInfectionRatio;
        }
        return Math.pow(ratio, 2) / Math.pow(RATIO_MAX, 2);
    }

    // TODO Redundancy with State seven Days Incidence Calculation?
    public void updateSevenDaysIncidence(){
        double sevenDaysIncidence =  getTotalActiveCases() / caseHistory.length;
        double sevenDaysIncidenceOn100k = (sevenDaysIncidence / this.getPopulation()) * 100000;

        this.sevenDaysIncidence = sevenDaysIncidenceOn100k;
    }

    public void updateHealedCases(int amountOfCases){
        this.healedCases += amountOfCases;
    }

    public void updateDeadCases(int amountOfCases){
        this.deadCases += amountOfCases;
    }

    public void reloadCity(){
        this.updateActiveCases();
        this.updateNewCases();
        this.updateRValue();
        this.updateSevenDaysIncidence();
    }

    public void updatePopulationLeftToInfect(int amountOfFirstCases){
        this.populationLeftFirstInfection = populationLeftFirstInfection - amountOfFirstCases;
    }

    public void setNewCases(int newCases) {
        this.newCases = newCases;
    }

    public void setCurrentVirus(Virus currentVirus) {
        this.currentVirus = currentVirus;
    }

    private double calculateActiveCasesInfectingSomeone(){

        double infectingRate = 0;

        // risks over the days to infect someone
        // TODO Move Array in Virus class
        double firstDay     = 0.8;
        double secondDay    = 0.7;
        double thirdDay     = 0.6;
        double fourthDay    = 0.4;
        double fifthDay     = 0.3;
        double sixthDay     = 0.2;
        double seventhDay   = 0.1;



        double[] daysLeft = {firstDay, secondDay, thirdDay, fourthDay, fifthDay, sixthDay, seventhDay};

        // if case history is filled with 6 elements
        // calculate infections for each day
        // TODO foreach Schleife -> vll Smarter
        if (this.caseHistory[6] != -1){
            infectingRate += this.caseHistory[0] * seventhDay;  // 20% probability on day 7
            infectingRate += this.caseHistory[1] * sixthDay;    // 30% probability on day 6
            infectingRate += this.caseHistory[2] * fifthDay;
            infectingRate += this.caseHistory[3] * fourthDay;
            infectingRate += this.caseHistory[4] * thirdDay;
            infectingRate += this.caseHistory[5] * secondDay;
            infectingRate += this.caseHistory[6] * firstDay;

            // TODO Multiplier für Abstandsregeln, Hygieneregeln
            return infectingRate;
        }

        // if case history is not fully filled
        for (int numberOfEntry = 6; numberOfEntry > 0; numberOfEntry--)
        {
            if (this.caseHistory[numberOfEntry] == -1 && this.caseHistory[numberOfEntry - 1] != -1){
                int day = 0;
                for (int amountOfElements = numberOfEntry; amountOfElements > 0; amountOfElements--){
                    infectingRate += this.caseHistory[amountOfElements - 1] * daysLeft[day];
                    day++;
                }
                return infectingRate;
            }
        }

        // if something went wrong
        return 111.111;
    }

    public void addToVaccinationProportion(float vaccinationGrowth) {
        if (this.vaccinationProportion + vaccinationGrowth < 100){
            this.vaccinationProportion += vaccinationGrowth;
        }
        else {
            this.vaccinationProportion = 100f;
        }
    }

    public void removeVaccinationProportion(float amountOfDecrease){
        if (this.vaccinationProportion - amountOfDecrease < 0){
            this.vaccinationProportion = 0;
        }
        else {
            this.vaccinationProportion -= amountOfDecrease;
        }
    }

    public void setFirstInfectionNewCases(int fristInfectionNewCases) {
        this.fristInfectionNewCases = fristInfectionNewCases;
    }

    public int getFristInfectionNewCases() {
        return fristInfectionNewCases;
    }

    public double getSevenDaysIncidence() {
        return sevenDaysIncidence;
    }

    public double getRValue() {
        return rValue;
    }

    public int getPopulation() {
        return population;
    }

    public int getPopulationDensity() {
        return populationDensity;
    }

    public float getVaccinationProportion() {
        return vaccinationProportion;
    }

    public int getDeadCases() {
        return deadCases;
    }

    public void setRValue(double rValue) {
        this.rValue = rValue;
    }

    public void setTotalNewCases(int totalNewCases) {
        this.totalNewCases = totalNewCases;
    }

    public int getTotalNewCases() {
        return totalNewCases;
    }

    public void calculateAndSetInfectionRatio(){
        this.cityInfectionRatio = (double)this.getTotalActiveCases() / (double)this.population;
    }

    public void removeFromPopulation(int deadCases){
        this.population -= deadCases;
    }

    public double getObedience() {
        return obedience;
    }

    public void setContactRestrictionsOfMotherState(double contactRestrictionsOfMotherState) {
        this.contactRestrictionsOfMotherState = contactRestrictionsOfMotherState;
    }

    public double getContactRestrictionsOfMotherState() {
        return contactRestrictionsOfMotherState;
    }

    public void setContactRestrictionDuration(int amountOfDays){
        this.contactRestrictionsDaysLeft = amountOfDays;
    }

    public int getContactRestrictionsDaysLeft() {
        return contactRestrictionsDaysLeft;
    }

    public void looseObedience(double lost){
        this.obedience += lost;
    }

    public void gainObedience(double gain){
        if (this.obedience - gain > 1){
            this.obedience -= gain;
        }
        else{
            this.obedience = 1;
        }

    }
}

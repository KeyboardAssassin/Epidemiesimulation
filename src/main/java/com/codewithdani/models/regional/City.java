package com.codewithdani.models.regional;

import com.codewithdani.models.histories.HealedHistory;
import com.codewithdani.models.threats.Virus;

import java.util.Arrays;
import java.util.Random;

public class City {
    private final String name;
    private final int population;
    private final int populationDensity;
    private int populationLeftFirstInfection;
    private int activeCases;
    private int newCases;
    private int fristInfectionNewCases;
    private int healedCases;
    private int deadCases;
    private double sevenDaysIncidence;
    private double rValue;
    private int[] caseHistory;
    private HealedHistory healedHistory;
    private Virus currentVirus;
    private float vaccinationProportion;

    // min and max amount a persons meets other persons per day (random)
    private static double minAmountOfMeetingsPerDay = 0.1;
    private static double maxAmountOfMeetingsPerDay = 2.4;

    // min and max amount of infected people for the first day (random)
    private static int firstDayInfectedPeopleMin = 1;
    private static int firstDayInfectedPeopleMax = 7;


    public City(String name, int population, int populationDensity) {
        this.name = name;
        this.population = population;
        this.populationDensity = populationDensity;
        this.sevenDaysIncidence = 0.0;
        this.rValue = 0.0;
        this.caseHistory = new int[]{-1, -1, -1, -1, -1, -1, -1};
        this.populationLeftFirstInfection = -1;
        this.currentVirus = new Virus("alpha", 100, 0.009);
        this.healedHistory = new HealedHistory();
        this.vaccinationProportion = 0.0f;
    }

    public String getName() {
        return name;
    }

    public void setHistoryDay(int day, int value){
        this.caseHistory[day - 1] = value;
    }

    public int calculateSum(){
        return Arrays.stream(caseHistory).filter(x -> x != -1).sum();
    }

    public void updateActiveCases(){
        this.activeCases = calculateSum();
    }

    public void addActiveCases(int newCases){
        this.activeCases += newCases;
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
        // if history contains less than 7 days
        for (int numberOfEntry = 6; numberOfEntry > 0; numberOfEntry--){
            if (caseHistory[numberOfEntry] != -1 && caseHistory[numberOfEntry - 1] != -1){
                this.rValue = (double)caseHistory[numberOfEntry] / (double)caseHistory[numberOfEntry - 1];
                return;
            }
        }
        // if history is fully filled
        this.rValue = (double)caseHistory[6] / (double)caseHistory[5];
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
        int deadCasesWithOutMedicine = this.calculateDeaths(country);
        int deadCasesAfterMedicine = this.tryToHealWithMedicine(country, deadCasesWithOutMedicine);
        int healedThroughMedicine = deadCasesWithOutMedicine - deadCasesAfterMedicine;

        // update Deaths & Healed Cases
        this.updateDeadCases(deadCasesAfterMedicine);
        this.updateHealedCases((int)(caseHistory[0] * (1 - currentVirus.getMortalityRate())) + healedThroughMedicine);

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

    public int calculateDeaths(Country country){
        // TODO usage of medicine
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

    public int calculateNextDayInfections(int day){
        Random r = new Random();

        int highestCityDensity = 4790; // TODO Methode, welche diese Daten setzt erstellen?
        int lowestCityDensity = 596;
        double maxPopulationDensityBoost = 0.3;
        double protectionAfterFirstInfection = 0.1; // 10% more safety if you had the virus 1 time

        // first day
        if (day == 0){
            return (r.nextInt(firstDayInfectedPeopleMax - firstDayInfectedPeopleMin) + firstDayInfectedPeopleMin);
        }

        // Probability between 0% and 30% depending on the density of the city (min/max: city with lowest/highest density)
        int differenceHighestAndLowestDensity = highestCityDensity - lowestCityDensity; // Densities Cottbus and München (min/max)
        double normalizedDensity = (this.populationDensity - lowestCityDensity);
        double populationDensityProbability =  (normalizedDensity / differenceHighestAndLowestDensity) * maxPopulationDensityBoost; // 4790 Density (München) equals factor of 30% = 0.3

        // Probability depending on the proportion of healed or vaccinated cases to the total population
        // every person who had the infection at least 1 time is 10% more protected
        double decreasingProbabilityGrowingRateOfCuredCases = (((this.populationLeftFirstInfection / (double)population) * protectionAfterFirstInfection) + (1 - protectionAfterFirstInfection));

        // Average amount of People a person meets every day
        double amountOfAveragePeopleMeetings = minAmountOfMeetingsPerDay + (maxAmountOfMeetingsPerDay - minAmountOfMeetingsPerDay) * r.nextDouble();

        // Probability someone in the 7 days history infects someone
        double infectingCases = calculateActiveCasesInfectingSomeone();

        int amountOfPeopleWithAnotherInfection = this.healedHistory.calculateProbabilityOfAnotherInfection();

        // TODO Maßnahmen wie Isolation und Kontaktbeschränkungen auf aktive Fälle multiplizieren (einbeziehen)
        // TODO newFirstInfections sind mehr als totalNewInfections
        // calculation of the infections for the current day of the infection
        int newFirstInfections = (int)(infectingCases * ((populationDensityProbability + 1 ) * amountOfAveragePeopleMeetings));
        int totalNewInfections = (int)(newFirstInfections * decreasingProbabilityGrowingRateOfCuredCases) + amountOfPeopleWithAnotherInfection;

        this.setFirstInfectionNewCases(newFirstInfections);

        if (newFirstInfections > populationLeftFirstInfection)
        {
            this.setFirstInfectionNewCases(populationLeftFirstInfection);
            return populationLeftFirstInfection;
        } else {
            return totalNewInfections;
        }
    }

    public void updateSevenDaysIncidence(){
        this.sevenDaysIncidence = calculateSum() / caseHistory.length;
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
        if (populationLeftFirstInfection == -1){
            this.populationLeftFirstInfection = this.population;
            return;
        }
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
        double firstDay     = 0.9;
        double secondDay    = 0.8;
        double thirdDay     = 0.7;
        double fourthDay    = 0.5;
        double fifthDay     = 0.4;
        double sixthDay     = 0.3;
        double seventhDay   = 0.2;

        double[] daysLeft = {firstDay, secondDay, thirdDay, fourthDay, fifthDay, sixthDay, seventhDay};

        // if case history is filled with 6 elements
        // calculate infections for each day
        if (this.caseHistory[6] != -1){
            infectingRate += this.caseHistory[0] * seventhDay;  // 20% probability on day 7
            infectingRate += this.caseHistory[1] * sixthDay;    // 30% probability on day 6
            infectingRate += this.caseHistory[2] * fifthDay;
            infectingRate += this.caseHistory[3] * fourthDay;
            infectingRate += this.caseHistory[4] * thirdDay;
            infectingRate += this.caseHistory[5] * secondDay;
            infectingRate += this.caseHistory[6] * firstDay;

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

    public void setFirstInfectionNewCases(int fristInfectionNewCases) {
        this.fristInfectionNewCases = fristInfectionNewCases;
    }

    public int getFristInfectionNewCases() {
        return fristInfectionNewCases;
    }

    public double getSevenDaysIncidence() {
        return sevenDaysIncidence;
    }

    public double getrValue() {
        return rValue;
    }

    public int getPopulation() {
        return population;
    }

    public int getPopulationDensity() {
        return populationDensity;
    }
}

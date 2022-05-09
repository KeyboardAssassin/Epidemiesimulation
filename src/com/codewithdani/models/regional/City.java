package com.codewithdani.models.regional;

import com.codewithdani.models.threats.Virus;

import java.util.Arrays;
import java.util.Random;

public class City {
    private final String name;
    private final int population;
    private final int populationDensity;
    private int populationLeftToInfect;
    private int activeCases;
    private int newCases;
    private int healedCases;
    private int deadCases;
    private double sevenDaysIncidence;
    private double rValue;
    private int[] caseHistory;
    private Virus currentVirus;

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
        this.populationLeftToInfect = -1;
        this.currentVirus = new Virus("alpha", 100, 0.009);
    }

    public String getName() {
        return name;
    }

    public double getrValue() {
        return rValue;
    }

    public void setHistoryDay(int day, int value){
        this.caseHistory[day - 1] = value;
    }

    public int calculateSum(){
        int sum = 0;

        for (int i = 0; i < caseHistory.length; i++){
            if (caseHistory[i] != -1){
                sum += caseHistory[i];
            }
        }
        return sum;
    }

    public void updateActiveCases(){
        // todo check stream
        // this.activeCases = Arrays.stream(caseHistory).sum();                             // -> kalkuliert -1 mit ein
        // this.activeCases = Arrays.stream(caseHistory).filter(x -> x.value != -1).sum();  // -> x.value funktioniert nicht
        this.activeCases = calculateSum();
    }

    public void updateNewCases() {
        for (int i = 6; i > 0; i--) {
            if (caseHistory[i] != -1) {
                this.newCases = caseHistory[i];
                return;
            }
        }
    }

    public void updateRValue(){
        for (int i = 6; i > 0; i--){
            if (caseHistory[i] != -1 && caseHistory[i - 1] != -1){
                this.rValue = (double)caseHistory[i] / (double)caseHistory[i - 1];
                return;
            }
        }

        this.rValue = (double)caseHistory[6] / (double)caseHistory[5];
    }

    public void addNewEntryToHistory(int amountOfCases){
        for (int i = 0; i < caseHistory.length; i++){
            if (caseHistory[i] == -1){
                caseHistory[i] = amountOfCases;

                this.setNewCases(amountOfCases);
                this.updatePopulationLeftToInfect();

                return;
            }
        }
        this.updateHealedCases((int)(caseHistory[0] * (1 - currentVirus.getMortalityRate())));
        this.updateDeadCases((int)(caseHistory[0] * currentVirus.getMortalityRate()));

        // todo smartere Lösung finden
        caseHistory[0] = caseHistory[1];
        caseHistory[1] = caseHistory[2];
        caseHistory[2] = caseHistory[3];
        caseHistory[3] = caseHistory[4];
        caseHistory[4] = caseHistory[5];
        caseHistory[5] = caseHistory[6];
        caseHistory[6] = amountOfCases;

        this.setNewCases(amountOfCases);
        this.updatePopulationLeftToInfect();
    }

    public int getEntryFromHistory(int day){
        return caseHistory[day];
    }

    public int calculateNextDayInfections(int day){
        Random r = new Random();

        // first day
        if (day == 0){
            return (r.nextInt(firstDayInfectedPeopleMax - firstDayInfectedPeopleMin) + firstDayInfectedPeopleMin);
        }

        // Propability between 0% and 30% depending on the density of the city (min/max: city with lowest/highest density)
        int differenceHighestAndLowestDensity = 4790 - 596; // Densities Cottbus and München (min/max)
        double normalizedDensity = (this.populationDensity - 596);
        double populationProbabilty =  (normalizedDensity / differenceHighestAndLowestDensity) * 0.3; // 4790 Density (München) equals factor of 30% = 0.3

        // Probabilty depending on the proportion of healed or vaccinated cases to the total population
        double decreasingProbabiltyGrowingRateofHealedCases = (double)this.populationLeftToInfect / (double)population;

        // Average amount of People a person meets every day
        double amountOfAveragePeopleMeetings = minAmountOfMeetingsPerDay + (maxAmountOfMeetingsPerDay - minAmountOfMeetingsPerDay) * r.nextDouble();

        double infectingCases = calculateActiveCasesInfectingSomeone();

        // TODO Maßnahmen wie Isolation und Kontaktbeschränkungen auf aktive Fälle multiplizieren (einbeziehen)
        // calculation of the infections for the current day of the infection
        int infections = (int)(infectingCases * ((populationProbabilty + 1 ) * decreasingProbabiltyGrowingRateofHealedCases * amountOfAveragePeopleMeetings));

        if (newCases > populationLeftToInfect)
        {
            return populationLeftToInfect;
        } else {
            return infections;
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

    public void updatePopulationLeftToInfect(){
        if (populationLeftToInfect == -1){
            this.populationLeftToInfect = this.population;
            return;
        }
        this.populationLeftToInfect = populationLeftToInfect - newCases;
    }

    public void setNewCases(int newCases) {
        this.newCases = newCases;
    }

    public Virus getCurrentVirus() {
        return currentVirus;
    }

    public void setCurrentVirus(Virus currentVirus) {
        this.currentVirus = currentVirus;
    }

    private double calculateActiveCasesInfectingSomeone(){

        double infectingRate = 0;

        // risks over the days
        double FirstDay     = 0.9;
        double SecondDay    = 0.8;
        double ThirdDay     = 0.7;
        double FourthDay    = 0.5;
        double FifthDay     = 0.4;
        double SixthDay     = 0.3;
        double SeventhDay   = 0.2;

        double[] daysLeft = {FirstDay, SecondDay, ThirdDay, FourthDay, FifthDay, SixthDay, SeventhDay};

        if (this.caseHistory[6] != -1){
            infectingRate += this.caseHistory[0] * SeventhDay;  // 20% probability on day 7
            infectingRate += this.caseHistory[1] * SixthDay;    // 30% probability on day 6
            infectingRate += this.caseHistory[2] * FifthDay;
            infectingRate += this.caseHistory[3] * FourthDay;
            infectingRate += this.caseHistory[4] * ThirdDay;
            infectingRate += this.caseHistory[5] * SecondDay;
            infectingRate += this.caseHistory[6] * FirstDay;

            return infectingRate;
        }

        int amountOfDaysFilled = 0;

        for (int i = 6; i > 0; i--)
        {
            if (this.caseHistory[i] == -1 && this.caseHistory[i - 1] != -1){
                // amountOfDaysFilled = i;
                // System.out.println(amountOfDaysFilled + " SO VIELE TAGE ALLÄÄ");
                int day = 0;


                for (int amountOfElements = i; amountOfElements > 0; amountOfElements--){
                    infectingRate += this.caseHistory[amountOfElements - 1] * daysLeft[day];
                    day++;
                }
                return infectingRate;

            }
        }
        return 111.111;
    }
}
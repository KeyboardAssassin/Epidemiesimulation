package com.codewithdani.models.data;

import com.codewithdani.models.histories.HealedHistory;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.threats.Virus;

import java.util.Arrays;

public class InfectionData {
    private int activeCases;
    private int newCases;
    private int populationLeftFirstInfection;
    private int populationAlreadyHadFirstInfection = 0;
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
    private double cityInfectionRatio;

    public final static int NOT_INITIALISED = -1;

    private transient City thisCity;

    public InfectionData(City city, int population) {
        // TODO Reihenfolge überprüfen
        this.vaccinationProportion = 0.0f;
        this.currentVirus = Virus.ALPHA;
        this.caseHistory = new int[]{NOT_INITIALISED, NOT_INITIALISED, NOT_INITIALISED, NOT_INITIALISED, NOT_INITIALISED, NOT_INITIALISED, NOT_INITIALISED};
        this.rValue = 0.0;
        this.healedHistory = new HealedHistory();
        this.populationLeftFirstInfection = population;
        this.sevenDaysIncidence = 0.0;
        this.cityInfectionRatio = 0.0;
        thisCity = city;
    }

    public void updateSevenDaysIncidence(){
        // TODO Nur durch die Anzahl der caseHistoryElemente teilen die nicht -1 sind statt caseHistory.length!
        double sevenDaysIncidence =  getTotalActiveCases() / caseHistory.length;
        this.sevenDaysIncidence = (sevenDaysIncidence / thisCity.getPopulation()) * 100000;
    }

    public double getSevenDaysIncidence() {
        return sevenDaysIncidence;
    }

    public int getTotalActiveCases(){
        return Arrays.stream(caseHistory).filter(x -> x != -1).sum();
    }

    public void updateActiveCases(){
        this.activeCases = getTotalActiveCases();
    }

    public void calculateAndSetInfectionRatio(){
        this.cityInfectionRatio = (double)this.getTotalActiveCases() / (double)thisCity.getPopulation();
    }

    public void setHistoryDay(int day, int value){
        this.caseHistory[day - 1] = value;
    }

    public void setNewCases(int newCases) {
        this.newCases = newCases;
    }

    public void updateNewCases() {
        // go through every element backwards and set the first found at member variable newCase
        for (int elementOfHistory = 6; elementOfHistory > 0; elementOfHistory--) {
            if (caseHistory[elementOfHistory] != NOT_INITIALISED) {
                this.setNewCases(caseHistory[elementOfHistory]);
                return;
            }
        }
    }

    public void addNewEntryToHistory(int amountOfCases, Country country){
        int amountOfFirstCases = this.getFristInfectionNewCases();

        // calculate Deaths
        int deadCasesWithOutMedicine = this.calculateDeaths();
        int deadCasesAfterMedicine = this.tryToHealWithMedicine(country, deadCasesWithOutMedicine);
        int healedThroughMedicine = deadCasesWithOutMedicine - deadCasesAfterMedicine;

        // update Deaths & Healed Cases
        this.updateDeadCases(deadCasesAfterMedicine);
        this.updateHealedCases((int)(caseHistory[0] * (1 - currentVirus.getMortalityRate())) + healedThroughMedicine);
        thisCity.removeFromPopulation(deadCasesAfterMedicine);

        // todo smartere Lösung finden
        // move the oldest entry in the case history into the healed history
        if (caseHistory[6] != NOT_INITIALISED){
            healedHistory.addEntry(caseHistory[6]);
        }

        //  shift every element and add new cases at the first
        caseHistory[1] = caseHistory[0];
        caseHistory[2] = caseHistory[1];
        caseHistory[3] = caseHistory[2];
        caseHistory[4] = caseHistory[3];
        caseHistory[5] = caseHistory[4];
        caseHistory[6] = caseHistory[5];

        // set the newest record
        caseHistory[0] = amountOfCases;


        this.setNewCases(amountOfCases);
        this.updatePopulationLeftToInfect(amountOfFirstCases);
        this.updateInfectionData();
    }

    public int getFristInfectionNewCases() {
        return fristInfectionNewCases;
    }

    public int calculateDeaths(){
        if (caseHistory[6] != NOT_INITIALISED){
            // return calculated deaths
            return (int)(caseHistory[6] * currentVirus.getMortalityRate());
        }
        else{
            return 0;
        }
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
            country.getMeasure().getMedicine().useMedicine(amountOfDosesAvailable);
            peopleDying = (int)((peopleDying - amountOfDosesAvailable) + (amountOfDosesAvailable * deathProbabilityWithMedicine));
        }
        return peopleDying;
    }

    public void updateHealedCases(int amountOfCases){
        this.healedCases += amountOfCases;
    }

    public void updateDeadCases(int amountOfCases){
        this.deadCases += amountOfCases;
    }

    public void updatePopulationLeftToInfect(int amountOfFirstCases){
        this.populationLeftFirstInfection = populationLeftFirstInfection - amountOfFirstCases;
    }

    public int getPopulationLeftFirstInfection() {
        return populationLeftFirstInfection;
    }

    public void removePopulationLeftFirstInfection(int amountOfInfections){
        this.populationLeftFirstInfection -= amountOfInfections;
    }

    public void addPopulationAlreadyHadFirstInfection(int amountOfInfections){
        this.populationAlreadyHadFirstInfection += amountOfInfections;
    }

    public void setFirstInfectionNewCases(int fristInfectionNewCases) {
        this.fristInfectionNewCases = fristInfectionNewCases;
    }

    public void setTotalNewCases(int totalNewCases) {
        this.totalNewCases = totalNewCases;
    }

    public int getTotalNewCases() {
        return totalNewCases;
    }

    public int getDeadCases() {
        return deadCases;
    }

    public double getRValue() {
        return rValue;
    }

    public void setRValue(double rValue) {
        this.rValue = rValue;
    }

    public void updateRValue(){
        // calculates rValue if 7 days history is filled
        // comparison between smoothened 4 days mean
        // (old) elements 4, 5, 6 vs
        // (new) elements 0, 1, 2
        if (caseHistory[6] != NOT_INITIALISED) {
            double newValue = (caseHistory[0] + caseHistory[1] + caseHistory[2]) / 3;
            double oldValue = (caseHistory[4] + caseHistory[5] + caseHistory[6]) / 3;

            // TODO R-Wert unter 1
            setRValue(newValue / oldValue);
        }
        // if no day is filled or the second entry is 0 (cannot be divided by 0)
        else {
            setRValue(0);
        }
    }

    public int getEntryFromHistory(int day){
        return caseHistory[day - 1];
    }

    public double calculateActiveCasesInfectingSomeone(){
        double infectingRate = 0;

        // risks over the days to infect someone
        double[] infectionProbList = this.getCurrentVirus().getProbabilityListInfection();

        // 10% probability on day 7 || 20% probability on day 6
        for(int elementInArray = 0; elementInArray <= this.caseHistory.length - 1; elementInArray++) {
            if (this.caseHistory[elementInArray] != NOT_INITIALISED) {
                infectingRate += this.caseHistory[elementInArray] * infectionProbList[elementInArray];
            }
        }
        return infectingRate;
    }

    public Virus getCurrentVirus() {
        return currentVirus;
    }

    public void setCurrentVirus(Virus currentVirus) {
        this.currentVirus = currentVirus;
    }

    public void addToVaccinationProportion(float vaccinationGrowth) {
        if (this.vaccinationProportion + vaccinationGrowth < 1){
            this.vaccinationProportion += vaccinationGrowth;
        }
        else {
            this.vaccinationProportion = 1f;
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

    public float getVaccinationProportion() {
        return vaccinationProportion;
    }

    public double getCityInfectionRatio() {
        return cityInfectionRatio;
    }

    public HealedHistory getHealedHistory() {
        return healedHistory;
    }

    public void updateInfectionData(){
        this.updateActiveCases();
        this.updateNewCases();
        this.updateRValue();
        this.updateSevenDaysIncidence();
    }
}

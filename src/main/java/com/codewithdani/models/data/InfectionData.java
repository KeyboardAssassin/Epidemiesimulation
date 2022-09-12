package com.codewithdani.models.data;

import com.codewithdani.models.histories.HealedHistory;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.threats.Virus;

import java.util.LinkedList;
import java.util.stream.IntStream;

public class InfectionData {
    public static final int MAX_HISTORY_DAYS = 7;
    private int activeCases;
    private int populationLeftFirstInfection;
    private int populationAlreadyHadFirstInfection = 0;
    private int firstNewCases;
    private int totalNewCases;
    private int healedCases;
    private int totalDeadCases;
    private int currentDayDeadCases;
    private double sevenDaysIncidence = 0;
    private double rValue;
    private LinkedList<Integer> caseHistory = new LinkedList<>();
    private HealedHistory healedHistory;
    private Virus currentVirus;
    private boolean newVirus = true;
    private float vaccinationProportion;
    private double cityInfectionRatio;

    public final static int NOT_INITIALISED = -1;

    private transient City thisCity;

    public InfectionData(City city, int population) {
        this.populationLeftFirstInfection = population;
        this.sevenDaysIncidence = 0.0;
        this.rValue = 0.0;
        this.caseHistory.addAll(IntStream.range(0, MAX_HISTORY_DAYS).mapToObj(x -> NOT_INITIALISED).toList());
        this.healedHistory = new HealedHistory();
        this.currentVirus = Virus.ALPHA;
        this.vaccinationProportion = 0.0f;
        this.cityInfectionRatio = 0.0;
        thisCity = city;
    }

    public void updateSevenDaysIncidence(){
        this.sevenDaysIncidence = ((double)getTotalActiveCases() / thisCity.getPopulation()) * 100000;
    }

    public double getSevenDaysIncidence() {
        return sevenDaysIncidence;
    }

    public int getTotalActiveCases(){
        return caseHistory.stream().filter(x -> x != NOT_INITIALISED).mapToInt(Integer::intValue).sum();
    }

    public void updateActiveCases(){
        this.activeCases = getTotalActiveCases();
    }

    public void calculateAndSetInfectionRatio(){
        this.cityInfectionRatio = (double)this.getTotalActiveCases() / (double)thisCity.getPopulation();
    }

    public void setHistoryDay(int day, int value){
        caseHistory.set(day -1, value);
    }

    public void addNewEntryToHistory(int amountOfCases, Country country){
        // calculate Deaths
        int deadCasesWithOutMedicine = this.calculateDeaths();
        int deadCasesAfterMedicine = this.tryToHealWithMedicine(country, deadCasesWithOutMedicine);
        int healedThroughMedicine = deadCasesWithOutMedicine - deadCasesAfterMedicine;

        // update Deaths & Healed Cases
        this.updateDeadCases(deadCasesAfterMedicine);
        this.updateHealedCases((int)(caseHistory.getFirst() * (1 - currentVirus.getMortalityRate())) + healedThroughMedicine);
        thisCity.removeFromPopulation(deadCasesAfterMedicine);

        // move the oldest entry in the case history into the healed history
        if (caseHistory.size() == MAX_HISTORY_DAYS){
            healedHistory.addEntry(caseHistory.getLast());
        }

        this.updateRValue(amountOfCases);

        // set the newest record
        caseHistory.addFirst(amountOfCases);

        if (caseHistory.size() > MAX_HISTORY_DAYS) {
            caseHistory.removeLast();
        }
        this.updateInfectionData();
    }

    public int getFirstNewCases() {
        return firstNewCases;
    }

    public int calculateDeaths(){
        if (caseHistory.getLast() != NOT_INITIALISED){
            // return calculated deaths
            return (int)(caseHistory.getLast() * currentVirus.getMortalityRate());
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
        updateDayDeadCases(amountOfCases);
        updateTotalDeadCases(amountOfCases);
    }

    public void updateTotalDeadCases(int amountOfCases){
        this.totalDeadCases += amountOfCases;
    }

    public void updateDayDeadCases(int amountOfCases){
        this.currentDayDeadCases = amountOfCases;
    }

    public int getCurrentDayDeadCases() {
        return currentDayDeadCases;
    }

    public int getPopulationLeftFirstInfection() {
        return populationLeftFirstInfection;
    }

    public void removePopulationLeftFirstInfection(int amountOfInfections){
        if (populationLeftFirstInfection - amountOfInfections < 0){
            populationLeftFirstInfection = 0;
        }else{
            this.populationLeftFirstInfection -= amountOfInfections;
        }
    }

    public void addPopulationAlreadyHadFirstInfection(int amountOfInfections){
        this.populationAlreadyHadFirstInfection += amountOfInfections;
    }

    public void setFirstNewCases(int fristInfectionNewCases) {
        this.firstNewCases = fristInfectionNewCases;
    }

    public void setTotalNewCases(int totalNewCases) {
        this.totalNewCases = totalNewCases;
    }

    public int getTotalNewCases() {
        return totalNewCases;
    }

    public int getTotalDeadCases() {
        return totalDeadCases;
    }

    public double getRValue() {
        return rValue;
    }

    public void setRValue(double rValue) {
        this.rValue = rValue;
    }

    public void updateRValue(int amountOfCases){
        double amountOfDaysDivided = 4d;
        // calculates rValue if 7 days history is filled
        // comparison between smoothened 4 days mean
        // (new) elements this day new cases, 0, 1, 2
        // (old) elements 3, 4, 5, 6 vs
        if (caseHistory.getLast() != NOT_INITIALISED) {
            double newValue = (amountOfCases + caseHistory.get(0) + caseHistory.get(1) + caseHistory.get(2)) / amountOfDaysDivided;
            double oldValue = (caseHistory.get(3) + caseHistory.get(4) + caseHistory.get(5) + caseHistory.get(6)) / amountOfDaysDivided;

            if (oldValue == 0){
                setRValue(0);
            }else {
                setRValue(newValue / oldValue);
            }
        }
        // if no day is filled or the second entry is 0 (cannot be divided by 0)
        else {
            setRValue(0);
        }
    }

    public int getEntryFromHistory(int day){
        return caseHistory.get(day - 1);
    }

    public double calculateActiveCasesInfectingSomeone(){
        double infectingRate = 0;

        // risks over the days to infect someone
        double[] infectionProbList = this.getCurrentVirus().getProbabilityListInfection();

        // combine every element with its probability of transmission
        for(int elementInArray = 0; elementInArray <= this.caseHistory.size() - 1; elementInArray++) {
            if (this.caseHistory.get(elementInArray) != NOT_INITIALISED) {
                infectingRate += this.caseHistory.get(elementInArray) * infectionProbList[elementInArray];
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

    public void addToVaccinationProportion(double vaccinationGrowth) {
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
        this.updateSevenDaysIncidence();
    }

    public void setPopulationAlreadyHadFirstInfection(int populationAlreadyHadFirstInfection) {
        this.populationAlreadyHadFirstInfection = populationAlreadyHadFirstInfection;
    }

    public void setPopulationLeftFirstInfection(int populationLeftFirstInfection) {
        this.populationLeftFirstInfection = populationLeftFirstInfection;
    }

    public boolean isNewVirus(){
        return newVirus;
    }

    public void setNewVirus(boolean newVirus) {
        this.newVirus = newVirus;
    }

    public int getPopulationAlreadyHadFirstInfection() {
        return populationAlreadyHadFirstInfection;
    }
}

package com.codewithdani.models.regional;

public class City {
    private String name;
    private int population;
    private int populationLeftToInfect;
    private int populationDensity;
    private int activeCases;
    private int newCases;
    private int healedCases;
    private int deadCases;
    private double sevenDaysIncidence;
    private double rValue;
    private int[] caseHistory;

    public City(String name, int population, int populationDensity) {
        this.name = name;
        this.population = population;
        this.populationDensity = populationDensity;
        this.sevenDaysIncidence = 0.0;
        this.rValue = 0.0;
        this.caseHistory = new int[]{-1, -1, -1, -1, -1, -1, -1};
        this.populationLeftToInfect = -1;
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
        // this.activeCases = Arrays.stream(caseHistory).sum();

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
        // todo verschiebe alle und auf 6 kommt neuer Wert
        this.updateHealedCases((int)(caseHistory[0] * 0.8));
        this.updateDeadCases((int)(caseHistory[0] * 0.2));

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

    public int calculateNextDayInfections(){

        return 0;
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
}

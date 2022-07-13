package com.codewithdani.models.regional;

import com.codewithdani.models.actions.Measure;

import java.text.DecimalFormat;

public class Country {
    private final String name;
    private State[] states;

    public Measure measure;

    private double incidence;
    private double rValue;
    private int newInfections;
    private int newDeathCases;

    private int countryTotalPopulation;

    public Country(String name) {
        this.name = name;
        this.measure = new Measure();
    }

    public void setStates(State[] states) {
        this.states = states;
    }

    public String getName() {
        return name;
    }

    public int getAmountOfStates(){
        return states.length;
    }

    public State[] getStates() {
        return states;
    }

    public City getCityByName(String name){
        for (State state : states){
            for (City city : state.getCities()){
             if (city.getName().toLowerCase().equals(name)) return city;
            }
        }
        // TODO GEFAHR
        return null;
    }


    // TODO Ebenfalls
    public State getStateByName(String name){
        for (State state : states){
            if (state.getName().toLowerCase().equals(name)) return state;
        }

        return null;
    }

    public Measure getMeasure() {
        return measure;
    }

    public double getIncidence() {
        return incidence;
    }

    public String getIncidenceAsString(){
        double totalIncidence = this.getIncidence();
        return convertValueToStringWithDecimalFormat(totalIncidence);
    }

    // TODO Redundant otra vez?
    public String getRValueAsString(){
        double rValue = this.getRValue();
        return convertValueToStringWithDecimalFormat(rValue);
    }

    // TODO Redundant mit StatetListElement Methode?
    public String convertValueToStringWithDecimalFormat(double incidence){
        DecimalFormat df = new DecimalFormat("0.00");
        if (incidence % 1 == 0) return  String.valueOf((int)incidence); // TODO Smart?
        String resultIncidenceString = df.format(incidence);

        return resultIncidenceString;
    }

    public double getRValue() {
        return rValue;
    }

    public int getNewInfections() {
        return newInfections;
    }

    public int getNewDeathCases() {
        return newDeathCases;
    }

    public void setIncidence(double incidence) {
        this.incidence = incidence;
    }

    public void setRValue(double rValue) {
        this.rValue = rValue;
    }

    public void setNewInfections(int newInfections) {
        this.newInfections = newInfections;
    }

    public void setNewDeathCases(int newDeathCases) {
        this.newDeathCases = newDeathCases;
    }

    public void updateData(){
        try{
            setIncidence(this.calculateSummaryInfo("incidence"));
            setRValue(this.calculateSummaryInfo("rvalue"));
            setNewInfections((int)this.calculateSummaryInfo("newcases"));
            setNewDeathCases((int)this.calculateSummaryInfo("deadcases"));
        }
        catch (Exception e){
            System.out.println("Request from frontend to update country but country is not yet set!");
        }
    }

    public double calculateSummaryInfo(String type){
        double sumOfAllSevenDaysIncidences = 0.0;
        double sumOfAllRValues = 0.0;
        int sumOfAllNewInfections = 0;
        int sumOfAllNewDeathCases = 0;
        int amountOfCities = 0;
        int amountOfCitiesWithPositiveRValues = 0;

        // loops through every state and every city
        for (State state: states) {
            for(City city : state.getCities()){
                sumOfAllSevenDaysIncidences += city.getSevenDaysIncidence();
                if (city.getrValue() > 0){
                    sumOfAllRValues += city.getrValue();
                    amountOfCitiesWithPositiveRValues++;
                }
                sumOfAllNewInfections += city.getFristInfectionNewCases(); // TODO richtige membervariable? Oder braucht es noch eine new cases
                sumOfAllNewDeathCases += city.getDeadCases();
            }
            amountOfCities += state.getCities().length;
        }

        // outputs the median value of a requested value
        switch (type){
            case "incidence":
                return ((sumOfAllSevenDaysIncidences / amountOfCities) / this.getCountryTotalPopulation()) * 100000;
            case "rvalue":
                return sumOfAllRValues / amountOfCitiesWithPositiveRValues;
            case "newcases":
                return sumOfAllNewInfections;
            case "deadcases":
                return sumOfAllNewDeathCases;
            default:
                return -1.0;
        }
    }

    public void setCountryTotalPopulation() {
        int totalPopulation = 0;

        for (State state: this.getStates()) {
            for (City city: state.getCities()) {
                totalPopulation += city.getPopulation();
            }
        }

        this.countryTotalPopulation = totalPopulation;
    }

    public int getCountryTotalPopulation() {
        return countryTotalPopulation;
    }
}

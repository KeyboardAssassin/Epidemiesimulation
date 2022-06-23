package com.codewithdani.models.regional;

import java.text.DecimalFormat;

public class State {
    private final String name;
    private City[] cities;

    public State(String name) {
        this.name = name;
    }

    public void setCities(City[] cities) {
        this.cities = cities;
    }

    public City[] getCities() {
        return cities;
    }

    public String getName() {
        return name;
    }

    public String calculateSevenDaysIncidence(){
        DecimalFormat df = new DecimalFormat("0.00");
        double totalIncidence = 0.0;
        for (City city : cities){
            totalIncidence += city.getSevenDaysIncidence();
        }

        String resultIncidenceString = df.format(totalIncidence / cities.length);

        return resultIncidenceString;
    }

    public double getSevenDaysIncidence(){
        // TODO Redundancy
        double totalIncidence = 0.0;
        for (City city : cities){
            totalIncidence += city.getSevenDaysIncidence();
        }

        return totalIncidence / cities.length;
    }
}

package com.codewithdani.models.regional;

import java.text.DecimalFormat;

public class State {
    private final String name;
    private City[] cities;

    private double obedience = 1;

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

    public String outputSevenDaysIncidenceAsString(){
        double totalIncidence = this.getSevenDaysIncidence();
        DecimalFormat df = new DecimalFormat("0.00");
        String resultIncidenceString = df.format(totalIncidence);

        return resultIncidenceString;
    }

    public double getSevenDaysIncidence(){
        double totalIncidence = 0;
        for (City city : cities){
            totalIncidence += city.getSevenDaysIncidence();
        }

        return totalIncidence / cities.length;
    }

    public double getObedience() {
        return obedience;
    }
}

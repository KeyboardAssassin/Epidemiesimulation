package com.codewithdani.models.regional;

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
}

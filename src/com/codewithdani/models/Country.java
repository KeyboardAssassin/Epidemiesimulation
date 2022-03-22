package com.codewithdani.models;

public class Country {
    private String name;
    private State[] states;

    public Country(String name) {
        this.name = name;
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
}

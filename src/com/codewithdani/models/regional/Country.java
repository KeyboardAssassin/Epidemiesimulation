package com.codewithdani.models.regional;

import com.codewithdani.models.threats.Virus;

public class Country {
    private String name;
    public State[] states;

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

    public State[] getStates() {
        return states;
    }
}

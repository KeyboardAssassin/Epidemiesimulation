package com.codewithdani.models.regional;

import com.codewithdani.models.actions.Measure;
import com.codewithdani.models.threats.Virus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;

public class Country {
    private final String name;
    private State[] states;

    public Measure measure;

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
             if (city.getName().equals(name)) return city;
            }
        }
        return null;
    }

    public Measure getMeasure() {
        return measure;
    }
}

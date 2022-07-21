package com.codewithdani.models.threats;

public class Virus {
    private String name;
    private int infectionSpeed;
    private double mortalityRate;
    private double[] probabilityListInfection;


    public Virus(String name, int infectionSpeed, double mortalityRate, double[] infectionProbList) {
        this.name = name;
        this.infectionSpeed = infectionSpeed;
        this.probabilityListInfection = infectionProbList;
        this.mortalityRate = mortalityRate;
    }

    public double getMortalityRate() {
        return mortalityRate;
    }

    public double[] getProbabilityListInfection() {
        return probabilityListInfection;
    }
}

package com.codewithdani.models.threats;

public class Virus {
    // growth rate
    public String name;
    public int infectionSpeed;
    public double mortalityRate;

    public Virus(String name, int infectionSpeed, double mortalityRate) {
        this.name = name;
        this.infectionSpeed = infectionSpeed;
        this.mortalityRate = mortalityRate;
    }

    public double getMortalityRate() {
        return mortalityRate;
    }
}

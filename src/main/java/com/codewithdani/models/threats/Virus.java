package com.codewithdani.models.threats;

public enum Virus {
    ALPHA("alpha", 1.0, 0.009, new double[] {0.5, 0.4, 0.3, 0.2, 0.1, 0.05, 0.01}),
    BETA("beta", 0.9, 0.00216, new double[]{0.5, 0.38, 0.32, 0.18, 0.12, 0.01, 0.001}),
    DELTA("delta", 0.8, 0.003, new double[]{0.6, 0.3, 0.2, 0.1, 0.01, 0.001, 0.0001}),
    OMICRON("omicron", 1.1, 0.0041, new double[]{0.5, 0.35, 0.25, 0.15, 0.015, 0.0015, 0.003});

    private final String name;
    private final double infectionSpeed;
    private final double mortalityRate;
    private final double[] probabilityListInfection;

    Virus(String name, double infectionSpeed, double mortalityRate, double[] probabilityListInfection) {
        this.name = name;
        this.infectionSpeed = infectionSpeed;
        this.mortalityRate = mortalityRate;
        this.probabilityListInfection = probabilityListInfection;
    }

    public double getMortalityRate() {
        return mortalityRate;
    }

    public double[] getProbabilityListInfection() {
        return probabilityListInfection;
    }

}

package com.codewithdani.models.threats;

public enum Virus {
    ALPHA("alpha", 100, 0.009, new double[] {0.8, 0.7, 0.6, 0.4, 0.3, 0.2, 0.1}),
    BETA("beta", 100, 0.00216, new double[]{0.8, 0.7, 0.6, 0.4, 0.3, 0.2, 0.1}),
    DELTA("delta", 100, 0.003, new double[]{0.8, 0.7, 0.6, 0.4, 0.3, 0.2, 0.1}),
    OMICRON("omicron", 100, 0.0041, new double[]{0.8, 0.7, 0.6, 0.4, 0.3, 0.2, 0.1});

    private final String name;
    private final int infectionSpeed;
    private final double mortalityRate;
    private final double[] probabilityListInfection;

    Virus(String name, int infectionSpeed, double mortalityRate, double[] probabilityListInfection) {
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

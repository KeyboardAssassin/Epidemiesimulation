package com.codewithdani.api.models;

import com.codewithdani.models.regional.Country;
import com.codewithdani.util.SimulationUtils;

public class CountrySummaryTO {

    private final String incidence;
    private final String rValue;
    private final int newInfections;
    private final int newDeathCases;
    private final boolean vaccinationDeveloped;
    private final boolean vaccinationStarted;
    private final boolean medicationDeveloped;
    private final boolean medicationStarted;
    private final boolean simulationEnded;
    private final String currentVirusName;
    private final double currentVirusLethality;

    public CountrySummaryTO(Country country) {
        incidence = SimulationUtils.convertToStringWith2Digits(country.getIncidence());
        rValue = SimulationUtils.convertToStringWith2Digits(country.getRValue());
        newInfections = country.getNewInfections();
        newDeathCases = country.getNewDeathCases();
        vaccinationDeveloped = country.getMeasure().getVaccination().isVaccinationApproved();
        medicationDeveloped = country.getMeasure().getMedicine().isMedicineApproved();
        vaccinationStarted = country.getMeasure().getVaccination().isVaccinationStarted();
        medicationStarted = country.getMeasure().getMedicine().isMedicationStarted();
        simulationEnded = country.isEpidemicEnded();
        currentVirusName = country.getCurrentVirus().name();
        currentVirusLethality = Math.round((country.getCurrentVirus().getMortalityRate() * 100) * 100.0) / 100.0;
    }

    public String getIncidence() {
        return incidence;
    }

    public String getRValue() {
        return rValue;
    }

    public int getNewInfections() {
        return newInfections;
    }

    public int getNewDeathCases() {
        return newDeathCases;
    }

    public boolean isVaccinationDeveloped() {
        return vaccinationDeveloped;
    }

    public boolean isVaccinationStarted() {
        return vaccinationStarted;
    }

    public boolean isMedicationDeveloped() {
        return medicationDeveloped;
    }

    public boolean isMedicationStarted() {
        return medicationStarted;
    }

    public boolean isSimulationEnded() {
        return simulationEnded;
    }

    public String getCurrentVirusName() {
        return currentVirusName;
    }

    public double getCurrentVirusLethality() {
        return currentVirusLethality;
    }
}

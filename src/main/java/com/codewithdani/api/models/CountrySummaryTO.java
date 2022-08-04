package com.codewithdani.api.models;

import com.codewithdani.models.regional.Country;

public class CountrySummaryTO {

    private final String incidence;
    private final String rValue;
    private final int newInfections;
    private final int newDeathCases;
    private final boolean vaccinationDeveloped;
    private final boolean vaccinationStarted;
    private final boolean medicationDeveloped;
    private final boolean medicationStarted;

    public CountrySummaryTO(Country country) {
        incidence = country.getIncidenceAsString();
        rValue = country.getRValueAsString();
        newInfections = country.getNewInfections();
        newDeathCases = country.getNewDeathCases();
        vaccinationDeveloped = country.getMeasure().getVaccination().isVaccinationApproved();
        medicationDeveloped = country.getMeasure().getMedicine().isMedicineApproved();
        vaccinationStarted = country.getMeasure().getVaccination().isVaccinationStarted();
        medicationStarted = country.getMeasure().getMedicine().isMedicationStarted();
    }

    public String getIncidence() {
        return incidence;
    }

    public String getrValue() {
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
}

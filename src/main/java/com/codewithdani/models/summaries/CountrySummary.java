package com.codewithdani.models.summaries;

import com.codewithdani.models.regional.Country;

public class CountrySummary {

    private String incidence;
    private String rValue;
    private int newInfections;
    private int newDeathCases;
    private boolean vaccinationDeveloped;
    private boolean medicationDeveloped;

    public CountrySummary(Country country) {
        incidence = country.getIncidenceAsString();
        rValue = country.getRValueAsString();
        newInfections = country.getNewInfections();
        newDeathCases = country.getNewDeathCases();
        vaccinationDeveloped = country.getMeasure().getVaccination().isVaccinationApproved();
        medicationDeveloped = country.getMeasure().getMedicine().isMedicineApproved();
    }
}

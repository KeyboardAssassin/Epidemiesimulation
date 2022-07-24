package com.codewithdani.models.summaries;

import com.codewithdani.functionality.Util;
import com.codewithdani.models.regional.Country;

public class CountrySummary {

    private final String incidence;
    private final String rValue;
    private final int newInfections;
    private final int newDeathCases;
    private final boolean vaccinationDeveloped;
    private final boolean vaccinationStarted;
    private final boolean medicationDeveloped;
    private final boolean medicationStarted;

    public CountrySummary(Country country, Util util) {
        incidence = country.getIncidenceAsString(util);
        rValue = country.getRValueAsString(util);
        newInfections = country.getNewInfections();
        newDeathCases = country.getNewDeathCases();
        vaccinationDeveloped = country.getMeasure().getVaccination().isVaccinationApproved();
        medicationDeveloped = country.getMeasure().getMedicine().isMedicineApproved();
        vaccinationStarted = country.getMeasure().getVaccination().isVaccinationStarted();
        medicationStarted = country.getMeasure().getMedicine().isMedicationStarted();
    }
}

package com.codewithdani.models.summaries;

import com.codewithdani.models.regional.Country;

public class CountrySummary {

    private double incidence;
    private double rValue;
    private int newInfections;
    private int newDeathCases;

    public CountrySummary(Country country) {
        incidence = country.getIncidence();
        rValue = country.getrValue();
        newInfections = country.getNewInfections();
        newDeathCases = country.getNewDeathCases();
    }
}

package com.codewithdani.models.actions;

import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;

public class Measure {

    private boolean vaccinationApproved = false;
    private int dayOfDevelopmentStart = -1; // TODO: Decide if countdown is sufficient 90 days left -> 89 days left

    public void setVaccinationApproved(boolean vaccinationApproved) {
        this.vaccinationApproved = vaccinationApproved;
    }

    public void startDevelopingVaccination(Country country, int day){
        this.dayOfDevelopmentStart = day;
    }

    public boolean checkIfVaccinationIsDeveloped(int day){
        if (day > (dayOfDevelopmentStart + 90) && dayOfDevelopmentStart != -1) setVaccinationApproved(true);
        return vaccinationApproved;
    }

    public void vaccinatePeople(City city){

    }

    double activateContactRestriction(int time, String format){
        double calculatedFactor = 1.0;

        switch (format){
            case "day":
            case "weeks":
            case "months":
        }
        return 1.0;
    }
}

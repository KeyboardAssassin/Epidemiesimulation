package com.codewithdani.models.actions.goverment;

import com.codewithdani.models.regional.City;

import java.util.Random;

public class Vaccination {
    private int dayOfDevelopmentStart = -1; // TODO: Decide if countdown is sufficient 90 days left -> 89 days left
    private boolean vaccinationApproved = false;

    public void setVaccinationApproved(boolean vaccinationApproved) {
        this.vaccinationApproved = vaccinationApproved;
    }

    public void startDevelopingVaccination(int day){
        this.dayOfDevelopmentStart = day;
    }

    public boolean checkIfVaccinationIsDeveloped(int currentDay){
        // Source: Code 002 & Code 003
        int daysOfDevelopment = 330;
        if (currentDay > (dayOfDevelopmentStart + daysOfDevelopment) && dayOfDevelopmentStart != -1) setVaccinationApproved(true);
        return vaccinationApproved;
    }

    public boolean isVaccinationApproved() {
        return vaccinationApproved;
    }

    public void vaccinatePeople(City city){
        Random r = new Random();
        // TODO final variables in interface?
        float minVaccinatedOnOneDay = 0.1f; // in percent
        float maxVaccinatedOnOneDay = 0.8f;

        float actualVaccinatedOnOneDay = minVaccinatedOnOneDay + (maxVaccinatedOnOneDay - minVaccinatedOnOneDay) * r.nextFloat();

        // if (!checkIfVaccinationIsDeveloped(currentDay)) return;
        city.addToVaccinationProportion(actualVaccinatedOnOneDay);
    }
}

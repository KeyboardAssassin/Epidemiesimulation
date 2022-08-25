package com.codewithdani.models.actions.government;

import com.codewithdani.models.regional.City;

public class Vaccination {
    private int dayOfDevelopmentStart = -1; // TODO: Decide if countdown is sufficient 90 days left -> 89 days left
    private boolean vaccinationApproved = false;
    private boolean vaccinationStarted = false;
    private static final int DAYS_OF_VACCINATION_DEVELOPMENT = 4;
    private double vaccinationProtection = 0.1; // 10% protection

    public void setVaccinationApproved(boolean vaccinationApproved) {
        this.vaccinationApproved = vaccinationApproved;
    }

    public void startDevelopingVaccination(int day){
        this.dayOfDevelopmentStart = day;
    }

    public void checkIfVaccinationIsDeveloped(int currentDay){
        if (currentDay > (dayOfDevelopmentStart + DAYS_OF_VACCINATION_DEVELOPMENT) && dayOfDevelopmentStart != -1) setVaccinationApproved(true);
    }

    public boolean isVaccinationApproved() {
        return vaccinationApproved;
    }

    public void updateVaccination(City city){
        // TODO final variables in interface?
        float maxVaccinatedOnOneDay = 0.008f;
        float amountOfDecrease = 0.001f;

        // full maxVaccinatedOnOneDay if no one is vaccinated - will get lower if more people get vaccinated
        float vaccinationProportionThisDay = (1 - city.getInfectionData().getVaccinationProportion()) * maxVaccinatedOnOneDay;
        vaccinationProportionThisDay *= city.getObedience();

        city.getInfectionData().removeVaccinationProportion(amountOfDecrease);
        city.getInfectionData().addToVaccinationProportion(vaccinationProportionThisDay);
    }

    public boolean isVaccinationStarted() {
        return vaccinationStarted;
    }

    public void setVaccinationStarted(boolean vaccinationStarted) {
        this.vaccinationStarted = vaccinationStarted;
    }

    public double getVaccinationProtection() {
        return vaccinationProtection;
    }
}

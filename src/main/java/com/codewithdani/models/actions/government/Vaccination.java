package com.codewithdani.models.actions.government;

import com.codewithdani.models.regional.City;

public class Vaccination {
    private final static int NOT_INITIALISED = -1;
    private int dayOfDevelopmentStart = NOT_INITIALISED;
    private boolean vaccinationApproved = false;
    private boolean vaccinationStarted = false;
    private static final int DAYS_OF_VACCINATION_DEVELOPMENT = 4;
    private double VACCINATION_PROTECTION = 0.1; // 10% protection
    private final float MAX_VACCINATED_ON_ONE_DAY = 0.008f;
    private final float AMOUNT_OF_VACCINATION_DECREASE_ON_ONE_DAY = 0.001f;


    public void setVaccinationApproved(boolean vaccinationApproved) {
        this.vaccinationApproved = vaccinationApproved;
    }

    public void startDevelopingVaccination(int day){
        this.dayOfDevelopmentStart = day;
    }

    public void checkIfVaccinationIsDeveloped(int currentDay){
        if (currentDay > (dayOfDevelopmentStart + DAYS_OF_VACCINATION_DEVELOPMENT) && dayOfDevelopmentStart != NOT_INITIALISED) setVaccinationApproved(true);
    }

    public boolean isVaccinationApproved() {
        return vaccinationApproved;
    }

    public void updateVaccination(City city){
        // full maxVaccinatedOnOneDay if no one is vaccinated - will get lower if more people get vaccinated
        float vaccinationProportionThisDay = (1 - city.getInfectionData().getVaccinationProportion()) * MAX_VACCINATED_ON_ONE_DAY;
        vaccinationProportionThisDay *= city.getObedience();

        city.getInfectionData().removeVaccinationProportion(AMOUNT_OF_VACCINATION_DECREASE_ON_ONE_DAY);
        city.getInfectionData().addToVaccinationProportion(vaccinationProportionThisDay);
    }

    public boolean isVaccinationStarted() {
        return vaccinationStarted;
    }

    public void setVaccinationStarted(boolean vaccinationStarted) {
        this.vaccinationStarted = vaccinationStarted;
    }

    public double getVaccinationProtection() {
        return VACCINATION_PROTECTION;
    }
}

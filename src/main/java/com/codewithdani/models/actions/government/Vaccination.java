package com.codewithdani.models.actions.government;

import com.codewithdani.models.regional.City;

public class Vaccination {
    private int dayOfDevelopmentStart = -1; // TODO: Decide if countdown is sufficient 90 days left -> 89 days left
    private boolean vaccinationApproved = false;
    private boolean vaccinationStarted = false;
    private static final int DAYS_OF_VACCINATION_DEVELOPMENT = 4;
    private double vaccinationProtection = 0.9;

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
        float maxVaccinatedOnOneDay = 0.08f;
        float amountOfDecrease = 0.01f;

        double vaccinationDisobedienceExponentialMultiplier = 1; // wie schwierig ist es Leute vom Impfen zu überzeugen? e.g. 0 = egal, ob die Leute auf dich hören, lassen sie sich impfen (äußerst Positiv) 1 = neutrale Einstellung - Alles über 1 ist negativ

        float ratioOfVaccination = city.getVaccinationProportion();
        ratioOfVaccination += maxVaccinatedOnOneDay * Math.pow(city.getObedience(), vaccinationDisobedienceExponentialMultiplier);

        city.removeVaccinationProportion(amountOfDecrease);
        city.addToVaccinationProportion(ratioOfVaccination);
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

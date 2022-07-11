package com.codewithdani.models.actions.goverment;

import com.codewithdani.models.regional.City;

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

        // temporary value because of short simulations
        daysOfDevelopment = 7;
        if (currentDay > (dayOfDevelopmentStart + daysOfDevelopment) && dayOfDevelopmentStart != -1) setVaccinationApproved(true);
        return vaccinationApproved;
    }

    public boolean isVaccinationApproved() {
        return vaccinationApproved;
    }

    public void updateVaccination(City city){
        // TODO final variables in interface?
        float maxVaccinatedOnOneDay = 0.08f;
        float amountOfDecrease = 0.01f;

        double obedience = 1; // TODO In dem State speichern // wie sehr hören die Leute auf dich generell?
        double vaccinationDisobedienceExponentialMultiplier = 1; // wie schwierig ist es Leute vom Impfen zu überzeugen? e.g. 0 = egal, ob die Leute auf dich hören, lassen sie sich impfen (äußerst Positiv) 1 = neutrale Einstellung - Alles über 1 ist negativ

        float ratioOfVaccination = city.getVaccinationProportion();
        ratioOfVaccination += maxVaccinatedOnOneDay * Math.pow(obedience, vaccinationDisobedienceExponentialMultiplier);

        // if (!checkIfVaccinationIsDeveloped(currentDay)) return;
        city.removeVaccinationProportion(amountOfDecrease);
        city.addToVaccinationProportion(ratioOfVaccination);
    }
}

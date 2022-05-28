package com.codewithdani.models.actions.goverment;

import com.codewithdani.models.regional.Country;

public class Medicine {
    private int dayOfDevelopmentStart = -1;
    private boolean medicineApproved = false;
    private final int daysOfDevelopment = 730; // Source: Code 001

    public void setMedicineApproved(boolean medicineApproved) {
        this.medicineApproved = medicineApproved;
    }

    public void startDevelopingVaccination(Country country, int day){
        this.dayOfDevelopmentStart = day;
    }

    public boolean checkIfMedicineIsDeveloped(int currentDay){
        if (currentDay > (dayOfDevelopmentStart + daysOfDevelopment) && dayOfDevelopmentStart != -1) setMedicineApproved(true);
        return medicineApproved;
    }
}

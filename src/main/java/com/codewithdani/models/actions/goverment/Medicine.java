package com.codewithdani.models.actions.goverment;

import com.codewithdani.models.regional.Country;

public class Medicine {
    private int dayOfDevelopmentStart = -1;
    private boolean medicineApproved = false;
    private final int daysOfDevelopment = 730; // Source: Code 001
    private int medicineInStock = 0;

    public void setMedicineApproved(boolean medicineApproved) {
        this.medicineApproved = medicineApproved;
    }

    public void startDevelopingMedicine(int day){
        this.dayOfDevelopmentStart = day;
    }

    public boolean checkIfMedicineIsDeveloped(int currentDay){
        if (currentDay > (dayOfDevelopmentStart + daysOfDevelopment) && dayOfDevelopmentStart != -1) setMedicineApproved(true);
        return medicineApproved;
    }

    public void produceMedicine(){
        int medicineProducedPerDay = 20;
        this.medicineInStock += medicineProducedPerDay;
    }

    public boolean isMedicineApproved() {
        return medicineApproved;
    }
}

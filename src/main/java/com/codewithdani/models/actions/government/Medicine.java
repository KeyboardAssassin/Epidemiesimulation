package com.codewithdani.models.actions.government;

public class Medicine {
    private int dayOfDevelopmentStart = -1;
    private boolean medicineApproved = false;
    private boolean medicationStarted = false;
    private int medicineInStock = 0;
    private static final int DAYS_OF_DEVELOPMENT = 7; // Source: Code 001 - 730 days
    private static final double EFFECTIVITY_OF_MEDICINE = 0.2;


    public void setMedicineApproved(boolean medicineApproved) {
        this.medicineApproved = medicineApproved;
    }

    public void startDevelopingMedicine(int day){
        this.dayOfDevelopmentStart = day;
    }

    public void checkIfMedicineIsDeveloped(int currentDay){
        if (currentDay > (dayOfDevelopmentStart + DAYS_OF_DEVELOPMENT) && dayOfDevelopmentStart != -1) setMedicineApproved(true);
    }

    public void produceMedicine(){
        int medicineProducedPerDay = 20;
        this.medicineInStock += medicineProducedPerDay;
    }

    public boolean isMedicineApproved() {
        return medicineApproved;
    }

    public int getMedicineInStock() {
        return medicineInStock;
    }

    public void useMedicine(int numberOfUsedDoses){
        this.medicineInStock -= numberOfUsedDoses;
    }

    public double getEffectivityOfMedicine() {
        return EFFECTIVITY_OF_MEDICINE;
    }

    public boolean isMedicationStarted() {
        return medicationStarted;
    }

    public void setMedicationStarted(boolean medicationStarted) {
        this.medicationStarted = medicationStarted;
    }
}

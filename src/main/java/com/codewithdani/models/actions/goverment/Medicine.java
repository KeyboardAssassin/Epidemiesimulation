package com.codewithdani.models.actions.goverment;

public class Medicine {
    private int dayOfDevelopmentStart = -1;
    private boolean medicineApproved = false;
    private boolean medicationStarted = false;
    private final int daysOfDevelopment = 7; // Source: Code 001 - 730 days
    private int medicineInStock = 0;

    private double effectivityOfMedicine = 0.2;

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

    public int getMedicineInStock() {
        return medicineInStock;
    }

    public void useMedicine(int numberOfUsedDoses){
        this.medicineInStock -= numberOfUsedDoses;
    }

    public double getEffectivityOfMedicine() {
        return effectivityOfMedicine;
    }

    public void setEffectivityOfMedicine(double effectivityOfMedicine) {
        this.effectivityOfMedicine = effectivityOfMedicine;
    }

    public boolean isMedicationStarted() {
        return medicationStarted;
    }

    public void setMedicationStarted(boolean medicationStarted) {
        this.medicationStarted = medicationStarted;
    }
}

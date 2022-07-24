package com.codewithdani.models.actions;

import com.codewithdani.models.actions.goverment.Medicine;
import com.codewithdani.models.actions.goverment.Vaccination;
import com.codewithdani.models.actions.self.contact.Distancing;

public class Measure {
    Vaccination vaccination;
    Medicine medicine;
    Distancing distancing;

    public Measure() {
        this.vaccination = new Vaccination();
        this.medicine = new Medicine();
        this.distancing = new Distancing();
    }

    public Vaccination getVaccination() {
        return vaccination;
    }

    public Medicine getMedicine(){
        return medicine;
    }

    public Distancing getDistancing() {
        return distancing;
    }
}

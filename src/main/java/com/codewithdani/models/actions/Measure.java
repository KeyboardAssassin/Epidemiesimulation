package com.codewithdani.models.actions;

import com.codewithdani.models.actions.government.Medicine;
import com.codewithdani.models.actions.government.Vaccination;
import com.codewithdani.models.actions.self.Distancing;

public class Measure {
    Vaccination vaccination;
    Medicine medicine;

    public Measure() {
        this.vaccination = new Vaccination();
        this.medicine = new Medicine();
    }

    public Vaccination getVaccination() {
        return vaccination;
    }

    public Medicine getMedicine(){
        return medicine;
    }

}

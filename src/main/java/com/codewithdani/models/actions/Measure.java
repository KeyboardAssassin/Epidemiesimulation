package com.codewithdani.models.actions;

import com.codewithdani.models.actions.goverment.Medicine;
import com.codewithdani.models.actions.goverment.Vaccination;
import com.codewithdani.models.actions.self.contact.Tracing;
import com.codewithdani.models.actions.self.contact.Distancing;

public class Measure {

    Vaccination vaccination;
    Medicine medicine;
    Tracing tracing;
    Distancing distancing;

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

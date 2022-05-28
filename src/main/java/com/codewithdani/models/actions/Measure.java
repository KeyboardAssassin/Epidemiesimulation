package com.codewithdani.models.actions;

import com.codewithdani.models.actions.goverment.Vaccination;
import com.codewithdani.models.actions.self.contact.Tracing;
import com.codewithdani.models.actions.self.contact.Distancing;

public class Measure {

    Vaccination vaccination;
    Tracing tracing;
    Distancing distancing;

    public Vaccination getVaccination() {
        return vaccination;
    }
}

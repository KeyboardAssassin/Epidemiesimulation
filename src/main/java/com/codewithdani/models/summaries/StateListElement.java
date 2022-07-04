package com.codewithdani.models.summaries;

import com.codewithdani.models.regional.State;

public class StateListElement {
    private String name;
    private String incidence;

    public StateListElement(State state) {
        this.name = state.getName();
        this.incidence = state.outputSevenDaysIncidenceAsString();
    }

}

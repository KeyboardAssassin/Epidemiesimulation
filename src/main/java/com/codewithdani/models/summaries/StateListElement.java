package com.codewithdani.models.summaries;

import com.codewithdani.functionality.Util;
import com.codewithdani.models.regional.State;

public class StateListElement {
    private String name;
    private String incidence;

    public StateListElement(State state, Util util) {
        this.name = state.getName();
        this.incidence = util.convertIncidenceToStringWith2Digits(state.getSevenDaysIncidence());
    }

}

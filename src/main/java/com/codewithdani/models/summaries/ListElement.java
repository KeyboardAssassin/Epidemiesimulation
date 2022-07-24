package com.codewithdani.models.summaries;

import com.codewithdani.functionality.Util;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.State;

public class ListElement {
    private final String name;
    private final String incidence;

    public ListElement(City city, Util util) {
        this.name = city.getName();
        this.incidence = util.convertIncidenceToStringWith2Digits(city.getSevenDaysIncidence());
    }
    public ListElement(State state, Util util) {
        this.name = state.getName();
        this.incidence = util.convertIncidenceToStringWith2Digits(state.getSevenDaysIncidence());
    }
}

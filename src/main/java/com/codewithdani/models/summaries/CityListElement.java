package com.codewithdani.models.summaries;

import com.codewithdani.functionality.Util;
import com.codewithdani.models.regional.City;

public class CityListElement {
    private String name;
    private String incidence;

    public CityListElement(City city, Util util) {
        this.name = city.getName();
        this.incidence = util.convertIncidenceToStringWith2Digits(city.getSevenDaysIncidence());
    }
}

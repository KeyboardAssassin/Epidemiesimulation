package com.codewithdani.models.summaries;

import com.codewithdani.functionality.Util;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;

import java.util.ArrayList;

public class ListSummary {
    private ArrayList<ListElement> ListElements;
    private final Util util = new Util();

    public ListSummary() {
        this.ListElements = new ArrayList<>();
    }

    public void fillEveryState(Country country){
        for (State state: country.getStates()) {
            ListElement element = new ListElement(state, util);
            ListElements.add(element);
        }
    }

    public void fillEveryCity(Country country){
        for (State state: country.getStates()) {
            for (City city: state.getCities()) {
                ListElement element = new ListElement(city, util);
                ListElements.add(element);
            }
        }
    }

    public ArrayList<ListElement> getListElements() {
        return ListElements;
    }
}

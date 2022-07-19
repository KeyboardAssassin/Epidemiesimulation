package com.codewithdani.models.summaries;

import com.codewithdani.functionality.Util;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;

import java.util.ArrayList;

public class StateListSummary {
    private ArrayList<StateListElement> stateListElements;

    private Util util = new Util();

    public StateListSummary() {
        this.stateListElements = new ArrayList<>();
    }

    public void fillEveryState(Country country){
        for (State state: country.getStates()) {
            StateListElement element = new StateListElement(state, util);
            stateListElements.add(element);
        }
    }

    public ArrayList<StateListElement> getStateListElements() {
        return stateListElements;
    }
}

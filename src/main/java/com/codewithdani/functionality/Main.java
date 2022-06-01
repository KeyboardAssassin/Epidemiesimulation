package com.codewithdani.functionality;

import com.codewithdani.json.Json;
import com.codewithdani.models.actions.Measure;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;
import com.codewithdani.models.threats.Virus;


public class Main {
    public static void main() {
        Simulation simulation = new Simulation();
        simulation.startSimulation();
    }
}

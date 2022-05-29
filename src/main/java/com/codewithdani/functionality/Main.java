package com.codewithdani.functionality;

import com.codewithdani.json.Json;
import com.codewithdani.models.actions.Measure;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;
import com.codewithdani.models.threats.Virus;


public class Main {

    // viruses
    static Virus beta  = new Virus("beta", 100, 0.00216);
    static Virus delta = new Virus("delta", 100, 0.003);
    static Virus omicron = new Virus("omicron", 100, 0.0041);
    public static void main(String[] args) {

        // create the json Reader/Writer Object
        Json json = new Json();

        // create the ability to create Measures
        Measure measure = new Measure();

        // create an empty Country
        Country germany = new Country("Deutschland");

        // check if a json file of that country already exists
        if (!json.checkIfCountryJsonExists("germany")) {
            json.createPreExistingGermany();
        }
        germany = json.importCountryFromJson(germany);

        System.out.println("Erfolgreich gestartet");
        System.out.println("Geladenes Land: " + germany.getName());
        System.out.println(germany.getAmountOfStates() + " Bundesländer");

        int averagePandemicTime = 0;
        int amountOfSimulations = 100;
        int daysOfTestingPerPandemic = 365;
        int dayOfVaccinationDevelopmentStart = 30;
        int dayOfMedicineDevelopmentStart = 60;
        Country currentTestedCountry = germany;
        State currentTestedState;
        City currentTestedCity;

        for (int amountOfSimulation = 0; amountOfSimulation < amountOfSimulations; amountOfSimulation++){
            // growth algorithm for 1 year
            for (int currentDay = 0; currentDay < daysOfTestingPerPandemic; currentDay++){
                // TODO check how long these viruses were prevailing

                if (currentDay == dayOfVaccinationDevelopmentStart) measure.getVaccination().startDevelopingVaccination(currentDay);
                if (currentDay == dayOfMedicineDevelopmentStart) measure.getMedicine().startDevelopingMedicine(currentDay);

                // run the simulation for every state of germany
                for (State state : currentTestedCountry.getStates()) {
                    currentTestedState = state;

                    // run the simulation for every city of the current state
                    for (int numberOfCurrentCity = 0; numberOfCurrentCity < currentTestedState.getCities().length; numberOfCurrentCity++) {
                        // set the current City
                        currentTestedCity = currentTestedState.getCities()[numberOfCurrentCity];

                        // try to vaccinate people if the vaccination is developed
                        if (measure.getVaccination().isVaccinationApproved()){
                            measure.getVaccination().vaccinatePeople(currentTestedCity);
                        } else {
                            measure.getVaccination().checkIfVaccinationIsDeveloped(currentDay);
                        }

                        // try to produce medicine for severe cases
                        if (measure.getMedicine().isMedicineApproved()){
                            measure.getMedicine().produceMedicine();
                        } else {
                            measure.getMedicine().checkIfMedicineIsDeveloped(currentDay);
                        }

                        // change virus over the days
                        virusEvolution(currentDay, currentTestedCity);

                        currentTestedCity.addNewEntryToHistory(currentTestedCity.calculateNextDayInfections(currentDay));
                        currentTestedCity.reloadCity();

                        // end the pandemic + logging
                        if (checkIfEveryCityHasNoNewInfections(currentTestedCountry)) {
                            System.out.println("Pandemie beendet an Tag: " + currentDay);

                            // reset all cities to start a new simulation
                            germany = resetCountry(germany, json);
                            averagePandemicTime += currentDay;
                            break; // only breaking out of if statement? TODO
                        }
                    }
                    // System.out.println("Tag: " + currentDay + " von Bundesland " + currentTestedState.getName() + " abgeschlossen!");
                }
            }
        }
        System.out.println();
        System.out.println("Durchschnittliche Dauer einer Pandemie: " + averagePandemicTime / amountOfSimulations + " Tage");

    }

    static void virusEvolution(int day, City currentTestedCity){
        int amountOfActiveAlphaDays = 108;  // 01.09.2020 (alpha) -> 18.12.2020 (beta) | currently hard cut TODO Soft transition
        int amountOfActiveBetaDays  = 195;  // (beta) -> (delta)
        int amountOfActiveDeltaDays = 148;  // (delta) -> (omicron)

        // change virus after the 108th day (3 Months change from alpha -> beta)
        if (day > amountOfActiveAlphaDays) currentTestedCity.setCurrentVirus(beta);
        if (day > amountOfActiveAlphaDays + amountOfActiveBetaDays) currentTestedCity.setCurrentVirus(delta);
        if (day > amountOfActiveAlphaDays + amountOfActiveBetaDays + amountOfActiveDeltaDays) currentTestedCity.setCurrentVirus(omicron);
    }

    static boolean checkIfEveryCityHasNoNewInfections(Country country){
        for (State state : country.getStates()){
            for (City city: state.getCities()){
                if (city.getEntryFromHistory(6) == 0 && city.getEntryFromHistory(5) == 0){
                    return true;
                }
            }
        }
        return false;
    }

    static Country resetCountry(Country country, Json json){
        // TODO replace with JSON loading again
        return json.importCountryFromJson(country);
    }

}

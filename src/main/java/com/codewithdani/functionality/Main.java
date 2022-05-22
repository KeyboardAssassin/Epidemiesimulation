package com.codewithdani.functionality;

import com.codewithdani.json.Json;
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
        Json json = new Json();

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

        // set days precisely
        germany.getCityByName("Halle").setHistoryDay(1, 100);
        germany.getCityByName("Halle").setHistoryDay(2, 200);
        germany.getCityByName("Halle").setHistoryDay(3, 300);
        germany.getCityByName("Halle").setHistoryDay(4, 400);
        germany.getCityByName("Halle").setHistoryDay(5, 500);
        germany.getCityByName("Halle").setHistoryDay(6, 600);
        germany.getCityByName("Halle").setHistoryDay(7, 700);

        germany.getCityByName("Halle").reloadCity();

        germany.getCityByName("Rostock").addNewEntryToHistory(10);
        germany.getCityByName("Rostock").addNewEntryToHistory(100);
        germany.getCityByName("Rostock").addNewEntryToHistory(500);

        germany.getCityByName("München").reloadCity();

        int averagePandemicTime = 0;
        int amountOfSimulations = 100;
        int daysOfTestingPerPandemic = 365;
        State currentTestedState;
        City currentTestedCity;

        for (int amountOfSimulation = 0; amountOfSimulation < amountOfSimulations; amountOfSimulation++){
            // growth algorithm for 1 year
            for (int day = 0; day < daysOfTestingPerPandemic; day++){
                // TODO check how long these viruses were prevailing

                // run the simulation for every state of germany
                for (State state : germany.getStates()) {
                    currentTestedState = state;

                    // run the simulation for every city of the current state
                    for (int numberOfCurrentCity = 0; numberOfCurrentCity < currentTestedState.getCities().length; numberOfCurrentCity++) {
                        // set the current City
                        currentTestedCity = currentTestedState.getCities()[numberOfCurrentCity];

                        // change virus over the days
                        virusEvolution(day, currentTestedCity);

                        currentTestedCity.addNewEntryToHistory(currentTestedCity.calculateNextDayInfections(day));
                        currentTestedCity.reloadCity();

                        // end the pandemic + logging
                        if (checkIfEveryCityHasNoNewInfections(germany)) {
                            System.out.println("Pandemie beendet an Tag: " + day);

                            // reset all cities to start a new simulation
                            resetAllCities();
                            averagePandemicTime += day;
                            break;
                        }
                    }
                    System.out.println("Tag: " + day + " von Bundesland " + currentTestedState.getName() + " abgeschlossen!");
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

    static void resetAllCities(){

    }

}

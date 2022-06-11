package com.codewithdani.functionality;

import com.codewithdani.json.JsonHandler;
import com.codewithdani.models.actions.Measure;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;
import com.codewithdani.models.threats.Virus;

public class Simulation {
    static Virus beta  = new Virus("beta", 100, 0.00216);
    static Virus delta = new Virus("delta", 100, 0.003);
    static Virus omicron = new Virus("omicron", 100, 0.0041);
    int sleepTime = 2000;
    int day = 0;

    Country simulatedCountry;


    public void startSimulation(int amountOfSimulations){
        // create the json Reader/Writer Object
        JsonHandler jsonHandler = new JsonHandler();

        // create the ability to create Measures
        Measure measure = new Measure();

        // create an empty Country
        Country germany = new Country("Deutschland");

        // check if a json file of that country already exists
        if (!jsonHandler.checkIfCountryJsonExists("germany")) {
            jsonHandler.createPreExistingGermany();
        }
        germany = jsonHandler.importCountryFromJson(germany);

        System.out.println("Erfolgreich gestartet");
        System.out.println("Geladenes Land: " + germany.getName());
        System.out.println(germany.getAmountOfStates() + " Bundesländer");

        int averagePandemicTime = 0;
        int daysOfTestingPerPandemic = 365;
        int dayOfVaccinationDevelopmentStart = 30;
        int dayOfMedicineDevelopmentStart = 60;
        simulatedCountry = germany;
        State currentTestedState;
        City currentTestedCity;

        for (int amountOfSimulation = 0; amountOfSimulation < amountOfSimulations; amountOfSimulation++){
            simulatedCountry = germany;

            // growth algorithm for 1 year
            for (int currentDay = 0; currentDay < daysOfTestingPerPandemic; currentDay++){

                this.setDay(currentDay);

                if (currentDay == dayOfVaccinationDevelopmentStart) measure.getVaccination().startDevelopingVaccination(currentDay);
                if (currentDay == dayOfMedicineDevelopmentStart) measure.getMedicine().startDevelopingMedicine(currentDay);

                // run the simulation for every state of germany
                for (State state : simulatedCountry.getStates()) {
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

                        currentTestedCity.addNewEntryToHistory(currentTestedCity.calculateNextDayInfections(currentDay), simulatedCountry);
                        currentTestedCity.reloadCity();
                    }
                    // Logging of every day if wanted
                    System.out.println("Tag: " + currentDay + " von Bundesland " + currentTestedState.getName() + " abgeschlossen!");
                }

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // end the pandemic + logging
                if (checkIfEveryCityHasNoNewInfections(simulatedCountry)) {
                    // System.out.println("Pandemie beendet an Tag: " + currentDay);

                    // reset all cities to start a new simulation
                    germany = resetCountry(germany, jsonHandler);
                    averagePandemicTime += currentDay;
                    // start a new simulation
                    break;
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
                // TODO Das ist nicht smart
                if (city.getEntryFromHistory(7) != 0 || city.getEntryFromHistory(6) != 0 || city.getEntryFromHistory(5) != 0 || city.getEntryFromHistory(4) != 0 || city.getEntryFromHistory(3) != 0 || city.getEntryFromHistory(2) != 0 || city.getEntryFromHistory(1) != 0){
                    return false;
                }

                // city.getSevenDaysIncidence() == 0
            }
        }
        return true;
    }

    static Country resetCountry(Country country, JsonHandler jsonHandler){
        // TODO replace with JSON loading again
        return jsonHandler.importCountryFromJson(country);
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Country getSimulatedCountry() {
        return simulatedCountry;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }
}

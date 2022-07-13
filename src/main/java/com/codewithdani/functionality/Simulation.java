package com.codewithdani.functionality;

import com.codewithdani.json.JsonHandler;
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

        // create an empty Country
        Country germany = new Country("Deutschland");

        // check if a json file of that country already exists
        if (!jsonHandler.checkIfCountryJsonExists("germany")) {
            jsonHandler.createPreExistingGermany();
        }
        germany = jsonHandler.importCountryFromJson(germany);
        germany.setCountryTotalPopulation();

        System.out.println("Erfolgreich gestartet");
        System.out.println("Geladenes Land: " + germany.getName());
        System.out.println(germany.getAmountOfStates() + " Bundesländer");

        int averagePandemicTime = 0;
        int daysOfTestingPerPandemic = 365;
        simulatedCountry = germany;
        City currentTestedCity;

        for (int amountOfSimulation = 0; amountOfSimulation < amountOfSimulations; amountOfSimulation++){
            simulatedCountry = germany;

            // growth algorithm for 1 year
            for (int currentDay = 0; currentDay < daysOfTestingPerPandemic; currentDay++){

                this.setDay(currentDay);

                // run the simulation for every state of germany
                for (State currentTestedState : simulatedCountry.getStates()) {

                    if (currentDay == 0){
                        currentTestedState.calculateStatePopulation();
                    }

                    // calculate infection ratio for current state
                    currentTestedState.calculateAndSetInfectedPopulation();
                    currentTestedState.calculateAndSetInfectionRatio();

                    // run the simulation for every city of the current state
                    for (int numberOfCurrentCity = 0; numberOfCurrentCity < currentTestedState.getCities().length; numberOfCurrentCity++) {
                        // set the current City
                        currentTestedCity = currentTestedState.getCities()[numberOfCurrentCity];

                        // try to vaccinate people if the vaccination is developed
                        // TODO Notification to frontend that vaccination is developed
                        if (simulatedCountry.getMeasure().getVaccination().isVaccinationApproved()){
                            simulatedCountry.getMeasure().getVaccination().updateVaccination(currentTestedCity);
                        } else {
                            simulatedCountry.getMeasure().getVaccination().checkIfVaccinationIsDeveloped(currentDay);
                        }

                        // try to produce medicine for severe cases
                        if (simulatedCountry.getMeasure().getMedicine().isMedicineApproved()){
                            simulatedCountry.getMeasure().getMedicine().produceMedicine();
                        } else {
                            simulatedCountry.getMeasure().getMedicine().checkIfMedicineIsDeveloped(currentDay);
                        }

                        // change virus over the days
                        virusEvolution(currentDay, currentTestedCity);

                        currentTestedCity.calculateAndSetInfectionRatio();

                        int nextDayInfections = currentTestedCity.calculateNextDayInfections(currentDay, currentTestedState.getStateInfectionRatio());

                        currentTestedCity.addNewEntryToHistory(nextDayInfections, simulatedCountry);
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
                if (checkIfEveryCityHasNoNewInfections(simulatedCountry) && this.day > 10) {
                    // System.out.println("Pandemie beendet an Tag: " + currentDay);

                    // reset all cities to start a new simulation
                    jsonHandler.createPreExistingGermany();
                    germany = jsonHandler.importCountryFromJson(germany);
                    averagePandemicTime += currentDay;
                    // TODO start a new simulation
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
        // check if every city has no new infections over the last 7 days
        for (State state : country.getStates()){
            for (City city: state.getCities()){
                if (city.getSevenDaysIncidence() != 0){
                    return false;
                }
            }
        }
        return true;
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

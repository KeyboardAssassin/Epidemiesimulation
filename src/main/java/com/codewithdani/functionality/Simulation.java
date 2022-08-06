package com.codewithdani.functionality;

import com.codewithdani.models.actions.government.Vaccination;
import com.codewithdani.util.SimulationJsonHandler;
import com.codewithdani.models.data.Data;
import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;
import com.codewithdani.models.threats.Virus;

import java.io.IOException;
import java.util.*;

import static com.codewithdani.models.actions.self.Distancing.SOCIAL_DISTANCING_VALUE;

public class Simulation {
    private String id = UUID.randomUUID().toString();

    int sleepTime = 2000;
    boolean simulationPause = false;
    int day = 0;
    Country simulatedCountry;
    // Data class
    Data data = new Data();

    Random random = new Random();

    boolean stopSimulation = false;

    Date creationDate = Calendar.getInstance().getTime();

    public void startSimulation(int amountOfSimulations) throws IOException {

        // create an empty Country
        Country germany;

        // check if a json file of that country already exists
        if (SimulationJsonHandler.checkIfCountryJsonExists("germany")) {
            germany = SimulationJsonHandler.importCountryFromJson("germany");
        } else {
           germany = SimulationJsonHandler.createPreExistingGermany();
        }


        System.out.println("Erfolgreich gestartet");
        System.out.println("Geladenes Land: " + germany.getName());
        System.out.println(germany.getAmountOfStates() + " Bundesländer");

        int averagePandemicTime = 0;
        int daysOfTestingPerPandemic = 365;
        simulatedCountry = germany;

        boolean allStatesSocialDistancingSet = false;

        // execute all setting methods
        initialiseDataClass();

        for (int amountOfSimulation = 0; amountOfSimulation < amountOfSimulations; amountOfSimulation++){

            // growth algorithm for 1 year
            for (int currentDay = 0; currentDay < daysOfTestingPerPandemic; currentDay++){
                if (stopSimulation) {
                    break;
                }

                this.setDay(currentDay);

                while (simulationPause){
                    // do nothing while pause
                }

                // handling for Social Distancing
                if (simulatedCountry.isSocialDistancingActivated() && !allStatesSocialDistancingSet){
                    for (State state: simulatedCountry.getStates()) {
                        if (state.getContactRestrictions() < SOCIAL_DISTANCING_VALUE){
                            state.setContactRestrictions(SOCIAL_DISTANCING_VALUE);
                        }
                        for (City city: state.getCities()) {
                            if (city.getContactRestrictions() < SOCIAL_DISTANCING_VALUE){
                                city.setContactRestrictions(SOCIAL_DISTANCING_VALUE);
                            }
                        }
                    }
                    allStatesSocialDistancingSet = true;
                }

                // count down days left of restrictions
                // if restrictions ends then reset it to 0
                restrictionCalculations(Arrays.asList(simulatedCountry.getStates()));

                // run the simulation for every state of germany
                for (State currentTestedState : simulatedCountry.getStates()) {
                    // Update the state information
                    currentTestedState.updateState();

                    if (currentDay == 0){
                        currentTestedState.updateTotalPopulation();
                    }

                    // calculate infection ratio for current state
                    currentTestedState.calculateAndSetInfectedPopulation();
                    currentTestedState.calculateAndSetInfectionRatio();


                    for (City city : currentTestedState.getCities()) {// try to vaccinate people if the vaccination is developed and the vaccination campaign started
                        // Usage of Vaccination & Medicine
                        vaccinationHandling(currentDay, city);
                        medicineHandling(currentDay);

                        // change virus over the days
                        virusEvolution(currentDay, simulatedCountry);

                        city.getInfectionData().calculateAndSetInfectionRatio();

                        int nextDayInfections = city.calculateNextDayInfections(currentDay, currentTestedState.getStateInfectionRatio(), data, random, this.getSimulatedCountry().getMeasure());

                        city.getInfectionData().addNewEntryToHistory(nextDayInfections, simulatedCountry);
                    }

                    // Update median obedience of all cities onto the state
                    currentTestedState.updateObedience();

                }
                // Logging of every day if wanted
                System.out.println(getId() + " Tag: " + currentDay + " abgeschlossen!");

                this.getSimulatedCountry().updateData();


                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // end the pandemic + logging
                if (checkIfEveryCityHasNoNewInfections(simulatedCountry) && this.day > 10) {
                    System.out.println("Pandemie beendet an Tag: " + currentDay);

                    // reset all cities to start a new simulation
                    germany = SimulationJsonHandler.createPreExistingGermany();
                    averagePandemicTime += currentDay;
                    break;
                }
            }
        }
        System.out.println();
        System.out.println("Durchschnittliche Dauer einer Pandemie: " + averagePandemicTime / amountOfSimulations + " Tage");
    }

    private void vaccinationHandling(int currentDay, City city) {
        Vaccination vaccination = simulatedCountry.getMeasure().getVaccination();
        if (vaccination.isVaccinationApproved() && vaccination.isVaccinationStarted()) {
            vaccination.updateVaccination(city);
        } else {
            vaccination.checkIfVaccinationIsDeveloped(currentDay);
        }
    }

    private void medicineHandling(int currentDay) {
        // try to produce medicine for severe cases
        if (simulatedCountry.getMeasure().getMedicine().isMedicineApproved() && simulatedCountry.getMeasure().getMedicine().isMedicationStarted()) {
            simulatedCountry.getMeasure().getMedicine().produceMedicine();
        } else {
            simulatedCountry.getMeasure().getMedicine().checkIfMedicineIsDeveloped(currentDay);
        }
    }

    private void restrictionCalculations(List<State> states) {
        for (State state : states) {
            int daysLeft = state.getContactRestrictionsDaysLeft();
            double obedienceLostPerDay = 0.08;
            double obedienceGainPerDay = 0.02;

            if (daysLeft > 0) {
                state.setContactRestrictionDuration(daysLeft - 1);

                state.removeObedienceFromAllCities(obedienceLostPerDay);
            }
            // if contact restrictions end
            if (daysLeft - 1 == 0) {
                if (simulatedCountry.isSocialDistancingActivated()) {
                    state.setContactRestrictionDuration(0);
                    state.setContactRestrictions(SOCIAL_DISTANCING_VALUE);
                    state.updateAllCitiesContactRestrictions(SOCIAL_DISTANCING_VALUE);
                } else {
                    // TODO Beide Methoden ggf. zusammenlegen
                    state.setContactRestrictions(0);
                    state.updateAllCitiesContactRestrictions(0);
                }
                state.removeObedienceFromAllCities(obedienceLostPerDay);
            }
            // if no restrictions are active check if the city got restrictions
            if (daysLeft == 0) {
                for (City city : state.getCities()) {
                    daysLeft = city.getContactRestrictionsDaysLeft();
                    if (daysLeft - 1 == 0) {
                        if (simulatedCountry.isSocialDistancingActivated()) {
                            city.setContactRestrictionDuration(0);
                            city.setContactRestrictions(SOCIAL_DISTANCING_VALUE);
                        } else {
                            city.setContactRestrictionDuration(0);
                            city.setContactRestrictions(0);
                        }

                        // loose obedience (last day of restrictions)
                        city.loseObedience(obedienceLostPerDay);
                    } else if (daysLeft > 0) {
                        city.setContactRestrictionDuration(daysLeft - 1);
                        city.loseObedience(obedienceLostPerDay);
                    } else if (daysLeft == 0) {
                        city.gainObedience(obedienceGainPerDay);
                    }
                }
            }
        }
    }

    static void virusEvolution(int day, Country simulatedCountry){
        int amountOfActiveAlphaDays = 108;  // 01.09.2020 (alpha) -> 18.12.2020 (beta) | currently hard cut TODO Soft transition
        int amountOfActiveBetaDays  = 195;  // (beta) -> (delta)
        int amountOfActiveDeltaDays = 148;  // (delta) -> (omicron)

        // change virus after the 108th day (3 Months change from alpha -> beta)
        if (day > amountOfActiveAlphaDays + amountOfActiveBetaDays + amountOfActiveDeltaDays){
            simulatedCountry.setCurrentVirus(Virus.OMICRON);
        }
        else if (day > amountOfActiveAlphaDays + amountOfActiveBetaDays){
            simulatedCountry.setCurrentVirus(Virus.DELTA);
        }
        else if (day > amountOfActiveAlphaDays){
            simulatedCountry.setCurrentVirus(Virus.BETA);
        }
    }

    static boolean checkIfEveryCityHasNoNewInfections(Country country){
        // check if every city has no new infections over the last 7 days
        for (State state : country.getStates()){
            for (City city: state.getCities()){
                if (city.getInfectionData().getSevenDaysIncidence() != 0){
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

    public int getCurrentDay() {
        return day;
    }

    public void setSimulationPause(boolean simulationPause) {
        this.simulationPause = simulationPause;
    }

    public void initialiseDataClass(){
        this.data.setHighestCityDensity(simulatedCountry);
        this.data.setLowestCityDensity(simulatedCountry);
        this.data.setDifference();
    }

    public String getId() {
        return id;
    }

    public void setStopSimulation(boolean stopSimulation) {
        this.stopSimulation = stopSimulation;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}

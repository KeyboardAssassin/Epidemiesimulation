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

public class Simulation {
    private String id = UUID.randomUUID().toString();

    int sleepTime = 3000;
    boolean simulationPause = false;
    int day = 0;
    Country simulatedCountry;
    // Data class
    Data data = new Data();
    boolean stopSimulation = false;

    Date creationDate = Calendar.getInstance().getTime();

    public void startSimulation(int amountOfSimulations) throws IOException {
        // load custom country or create country from template
        loadCountryOrCreateGermanyFromTemplate();

        System.out.println("Erfolgreich gestartet");
        System.out.println("Geladenes Land: " + simulatedCountry.getName());
        System.out.println(simulatedCountry.getAmountOfStates() + " Bundesländer");

        int averagePandemicTime = 0;
        int daysOfTestingPerPandemic = 365;

        // execute all setting methods
        initialiseDataClass();

        for (int amountOfSimulation = 0; amountOfSimulation < amountOfSimulations; amountOfSimulation++){

            if (amountOfSimulation >= 1){
                // reset all cities to start a new simulation
                loadCountryOrCreateGermanyFromTemplate();
            }

            // growth algorithm for 1 year
            for (int currentDay = 0; currentDay < daysOfTestingPerPandemic; currentDay++){
                if (stopSimulation) {
                    break;
                }

                this.setDay(currentDay);

                while (simulationPause){
                    // do nothing while pause
                }
                restrictionCalculations(Arrays.asList(simulatedCountry.getStates()));

                // change virus over the days
                virusEvolution(currentDay, simulatedCountry);

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

                        city.getInfectionData().calculateAndSetInfectionRatio();

                        int nextDayInfections = city.calculateNextDayInfections(currentTestedState.getStateInfectionRatio(), data, this.getSimulatedCountry().getMeasure());

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
                if (checkIfEveryCityHasNoNewInfections(simulatedCountry) && "OMICRON".equals(simulatedCountry.getCityByName("Erfurt").getInfectionData().getCurrentVirus().name())) {
                    simulatedCountry.setEpidemicEnded(true);
                    System.out.println("Pandemie beendet an Tag: " + currentDay);

                    averagePandemicTime += currentDay;
                    break;
                }
            }
        }
        simulatedCountry.setEpidemicEnded(true);
        System.out.println();
        System.out.println("Durchschnittliche Dauer einer Pandemie: " + averagePandemicTime / amountOfSimulations + " Tage");
    }

    private void loadCountryOrCreateGermanyFromTemplate() throws IOException {
        if (SimulationJsonHandler.checkIfCountryJsonExists("germany")) {
            simulatedCountry = SimulationJsonHandler.importCountryFromJson("germany");
        } else {
            simulatedCountry = SimulationJsonHandler.createPreExistingGermany();
        }
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
        // count down days left of restrictions
        // if restrictions ends then reset it to 0
        for (State state : states) {
            int daysOfStateRestrictionsLeft = state.getContactRestrictionsDaysLeft();
            double obedienceLostPerDay = 0.05;
            double obedienceGainPerDay = 0.02;

            // state restrictions handling
            if (daysOfStateRestrictionsLeft > 0) {
                state.setContactRestrictionDuration(daysOfStateRestrictionsLeft - 1);
                state.removeObedienceFromAllCities(obedienceLostPerDay);
            }
            // if contact restrictions end
            if (daysOfStateRestrictionsLeft - 1 == 0) {
                state.resetStateAndEveryCityFromRestrictions(simulatedCountry.isSocialDistancingActivated());
                state.removeObedienceFromAllCities(obedienceLostPerDay);
            }

            // city restrictions handling
            if (daysOfStateRestrictionsLeft == 0) {
                for (City city : state.getCities()) {
                    int daysOfCityRestrictionsLeft = city.getContactRestrictionsDaysLeft();

                    // if no city restrictions are active
                    if (daysOfCityRestrictionsLeft == 0){
                        city.gainObedience(obedienceGainPerDay);
                    }
                    else { // if city restrictions are active
                        if (daysOfCityRestrictionsLeft > 1){
                            city.setContactRestrictionDuration(daysOfCityRestrictionsLeft - 1);
                        }
                        if (daysOfCityRestrictionsLeft == 1){
                            city.resetCityFromRestrictions(simulatedCountry.isSocialDistancingActivated());
                        }
                        city.loseObedience(obedienceLostPerDay);
                    }
                }
            }
        }
    }

    static void virusEvolution(int day, Country simulatedCountry){
        int amountOfActiveAlphaDays = 50;
        int amountOfActiveBetaDays  = 90;
        int amountOfActiveDeltaDays = 45;

        String currentVirusName = simulatedCountry.getCityByName("Erfurt").getInfectionData().getCurrentVirus().name();

        // change virus after the 108th day (3 Months change from alpha -> beta)
        if (day > amountOfActiveAlphaDays + amountOfActiveBetaDays + amountOfActiveDeltaDays && currentVirusName.equals("DELTA")){
            simulatedCountry.updateAllCityVirus(Virus.OMICRON);
            simulatedCountry.resetAlreadyOneInfectionData();
            System.out.println("Neues Virus: " + Virus.OMICRON.name());
        }
        else if (day > amountOfActiveAlphaDays + amountOfActiveBetaDays && currentVirusName.equals("BETA")){
            simulatedCountry.updateAllCityVirus(Virus.DELTA);
            simulatedCountry.resetAlreadyOneInfectionData();
            System.out.println("Neues Virus: " + Virus.DELTA.name());
        }
        else if (day > amountOfActiveAlphaDays && currentVirusName.equals("ALPHA")){
            simulatedCountry.updateAllCityVirus(Virus.BETA);
            simulatedCountry.resetAlreadyOneInfectionData();
            System.out.println("Neues Virus: " + Virus.BETA.name());
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

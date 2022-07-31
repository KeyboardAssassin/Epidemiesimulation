package com.codewithdani.models.histories;

import java.util.Arrays;

public class HealedHistory {
    private int[] history;
    public final static int NOT_INITIALISED = -1;

    public final static double LESS_PROTECTION_PER_DAY = 0.001666;

    public HealedHistory() {
        this.history = new int[30]; // TODO auf 30 Tage -> danach populationLeftToInfekt
        // TODO Macht man sowas?
        initializeHistory();
    }

    public void initializeHistory(){
        Arrays.fill(this.history, NOT_INITIALISED);
    }

    public void addEntry(int cases){
        if (this.history[this.history.length - 1] != NOT_INITIALISED){
            this.history = this.shiftElements();
        }
        this.history[0] = cases;
    }

    public int[] shiftElements(){
        // TODO Smarte Lösung finden
        int[] newArray = new int[this.history.length];

        // shift all elements downwards
        for (int element = 0; element < history.length - 1; element++){
            newArray[element + 1] = history[element];
        }
        return newArray;
    }

    public int calculateProbabilityOfAnotherInfection(){
        double amountOfPeopleCouldBeInfectedAgain = 0;

        for (int indexOfHistory = 0; indexOfHistory < history.length; indexOfHistory++)
        {
            if (history[indexOfHistory] != -1){
                // TODO Sinnvolle Anzahl an Leuten, die sich noch infizieren können sollte rauskommen
                amountOfPeopleCouldBeInfectedAgain += history[indexOfHistory] * indexOfHistory * LESS_PROTECTION_PER_DAY;
            }
        }
        return (int)amountOfPeopleCouldBeInfectedAgain;
    }
}

package com.codewithdani.models.histories;

import java.util.Arrays;

public class HealedHistory {
    private int[] history;

    public final static int NOT_INITIALISED = -1;

    public HealedHistory() {
        this.history = new int[200]; // TODO auf 30 Tage -> danach populationLeftToInfekt
        initializeHistory();
    }

    public void initializeHistory(){
        Arrays.fill(this.history, NOT_INITIALISED);
    }

    public void addEntry(int cases){
        if (this.history[this.history.length - 1] != NOT_INITIALISED){
            this.history = this.shiftElements();
        }

        this.history[this.history.length - 1] = cases;
    }

    public int[] shiftElements(){
        // TODO Smartere Lösung finden
        int[] newArray = new int[this.history.length];

        for (int i = this.history.length - 1; i > 0; i--){
            int newPosition = (i + (this.history.length - 1)) % this.history.length;
            newArray[newPosition] = this.history[i];
        }
        return newArray;
    }

    public int calculateProbabilityOfAnotherInfection(){
        double amountOfPeopleCouldBeInfectedAgain = 0;
        double lessProtectionPerDay = 0.001666;

        for (int indexOfHistory = history.length - 1; indexOfHistory > 0; indexOfHistory--)
        {
            if (history[indexOfHistory] != -1){
                amountOfPeopleCouldBeInfectedAgain += history[indexOfHistory] * (history.length - indexOfHistory) * lessProtectionPerDay;
            }
        }
        return (int)amountOfPeopleCouldBeInfectedAgain;
    }
}

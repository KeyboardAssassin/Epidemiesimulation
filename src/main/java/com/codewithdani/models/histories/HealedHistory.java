package com.codewithdani.models.histories;

import java.util.Arrays;

public class HealedHistory {
    private int[] history;

    public final static int NOT_INITIALISED = -1;

    public HealedHistory() {
        this.history = new int[200];
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
        int amountOfPeopleAgainInfected = 0;

        for (int elementOfTheHistory = history.length - 1; elementOfTheHistory > 0; elementOfTheHistory--)
        {
            if (history[elementOfTheHistory] != -1){
                // TODO Warum 0.001666?
                int amountOfPeopleInfectedOnThisDay = (int)(history[elementOfTheHistory] * 0.001666);
                history[elementOfTheHistory] -= amountOfPeopleInfectedOnThisDay;
                amountOfPeopleAgainInfected += amountOfPeopleInfectedOnThisDay;
            }
        }
        return amountOfPeopleAgainInfected;
    }
}

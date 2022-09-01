package com.codewithdani.models.histories;

import java.util.LinkedList;

public class HealedHistory {
    public static final int MAX_HISTORY_DAYS = 30;
    private LinkedList<Integer> history = new LinkedList<>();
    public final static double LESS_PROTECTION_PER_DAY = 0.001666;

    public void addEntry(int cases){
        history.addFirst(cases);
        if (history.size() > MAX_HISTORY_DAYS) {
            history.removeLast();
        }
    }

    public int calculateProbabilityOfAnotherInfection(){
        double amountOfPeopleCouldBeInfectedAgain = 0;

        for (int indexOfHistory = 0; indexOfHistory < history.size(); indexOfHistory++)
        {
                // TODO Sinnvolle Anzahl an Leuten, die sich noch infizieren können sollte rauskommen
                amountOfPeopleCouldBeInfectedAgain += history.get(indexOfHistory) * indexOfHistory * LESS_PROTECTION_PER_DAY;
        }

        return (int)amountOfPeopleCouldBeInfectedAgain;
    }
}

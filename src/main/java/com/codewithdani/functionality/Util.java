package com.codewithdani.functionality;

import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.State;

import java.text.DecimalFormat;

public class Util {

    public String convertIncidenceToStringWith2Digits(double totalIncidence){
        DecimalFormat df = new DecimalFormat("0.00");
        if (totalIncidence % 1 == 0) return  String.valueOf((int)totalIncidence);

        return df.format(totalIncidence);
    }

    // TODO Hier hin auslagern?
    public double calculateRValue(City[] cities){
        return 0.0;
    }

    public double calculateRValue(State[] states){
        return 0.0;
    }
}

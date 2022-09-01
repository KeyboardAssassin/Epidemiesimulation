package com.codewithdani.util;

import java.text.DecimalFormat;

public class SimulationUtils {

    public static String convertToStringWith2Digits(double value){
        DecimalFormat df = new DecimalFormat("0.00");
        if (value % 1 == 0) return  String.valueOf((int)value);

        return df.format(value);
    }
}

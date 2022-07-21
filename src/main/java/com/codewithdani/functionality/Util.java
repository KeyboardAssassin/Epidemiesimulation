package com.codewithdani.functionality;

import java.text.DecimalFormat;

public class Util {

    public String convertIncidenceToStringWith2Digits(double totalIncidence){
        DecimalFormat df = new DecimalFormat("0.00");
        if (totalIncidence % 1 == 0) return  String.valueOf((int)totalIncidence); // TODO Smart?

        return df.format(totalIncidence);
    }
}

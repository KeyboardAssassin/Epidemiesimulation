package com.codewithdani.functionality;

import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.threats.Virus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityTest {


    @Test
    public void settingAndGettingHistoryDatesTest(){
        City testCity = new City("Testcity", 100000, 100);

        // set days precisely
        testCity.getInfectionData().setHistoryDay(1, 100);
        testCity.getInfectionData().setHistoryDay(2, 200);
        testCity.getInfectionData().setHistoryDay(3, 300);
        testCity.getInfectionData().setHistoryDay(4, 400);
        testCity.getInfectionData().setHistoryDay(5, 500);
        testCity.getInfectionData().setHistoryDay(6, 600);
        testCity.getInfectionData().setHistoryDay(7, 700);

        testCity.getInfectionData().updateInfectionData();

        assertEquals(100, testCity.getInfectionData().getEntryFromHistory(1));
        assertEquals(200, testCity.getInfectionData().getEntryFromHistory(2));
        assertEquals(300, testCity.getInfectionData().getEntryFromHistory(3));
        assertEquals(400, testCity.getInfectionData().getEntryFromHistory(4));
        assertEquals(500, testCity.getInfectionData().getEntryFromHistory(5));
        assertEquals(600, testCity.getInfectionData().getEntryFromHistory(6));
        assertEquals(700, testCity.getInfectionData().getEntryFromHistory(7));
    }

    @Test
    void addingNewDataToHistoryTest() {
        Country testCountry = new Country("Testcountry");
        City testCity = new City("Testcity", 100000, 100);

        testCity.getInfectionData().addNewEntryToHistory(10, testCountry);
        testCity.getInfectionData().addNewEntryToHistory(100, testCountry);
        testCity.getInfectionData().addNewEntryToHistory(500, testCountry);

        assertEquals(10,  testCity.getInfectionData().getEntryFromHistory(3));
        assertEquals(100, testCity.getInfectionData().getEntryFromHistory(2));
        assertEquals(500, testCity.getInfectionData().getEntryFromHistory(1));
    }
}

package com.codewithdani.functionality;

import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;
import com.codewithdani.models.threats.Virus;


public class Main {

    public static void main(String[] args) {

        // Viren
        Virus beta  = new Virus("beta", 100, 0.00216);
        Virus delta = new Virus("delta", 100, 0.003);
        Virus omicron = new Virus("omicron", 100, 0.0041);

        // Städte
        // Bayern
        City ingolstadt = new City("Ingolstadt", 136981, 1027);
        City munchen = new City("München", 1472000, 4790);
        City nurnberg = new City("Nürnberg", 518365, 2765);

        // Thüringen
        City erfurt = new City("Erfurt", 213699, 792);
        City jena = new City("Jena", 111407, 965);

        // Hessen
        City frankfurt = new City("Frankfurt", 753056, 3077);
        City wiesbaden = new City("Wiesbaden", 278342, 1367);

        // Baden-Württemberg
        City stuttgart = new City("Stuttgart", 634830, 3040);
        City mannheim = new City("Mannheim", 309370, 2136);

        // Sachsen
        City dresden = new City("Dresden", 554649, 1693);
        City leipzig = new City("Leipzig", 587857, 2006);

        // Niedersachsen
        City hannover = new City("Hannover", 532163, 2614);
        City braunschweig = new City("Braunschweig", 248292, 1290);

        // Rheinlandpfalz
        City mainz = new City("Mainz", 217118,2222);
        City ludwigshafen = new City("Ludwigshafen", 171061, 2229);

        // Schleswigholstein
        City kiel = new City("Kiel", 247548, 2078);
        City luebeck = new City("Lübeck", 217198, 1008);

        // Saarland
        City saarbruecken = new City("Saarbrücken", 178151, 1071);
        City neunkirchen = new City("Neunkirchen", 46100, 612);

        // Berlin
        City berlinCity = new City("Berlin", 3645000, 4112);

        // Brandenburg
        City potsdam = new City("Potsdam", 178089, 967);
        City cottbus = new City("Cottbus", 100219, 596);

        // Bremen
        City bremenCity = new City("Bremen", 569352, 1781);

        // Nordrhein-Westfahlen
        City duesseldorf = new City("Düsseldorf", 619294, 2854);
        City koeln = new City("Köln", 1086000, 2675);

        // Hamburg
        City hamburgCity = new City("Hamburg", 1841000, 2453);

        // Mecklenburg-Vorpommern
        City schwerin = new City("Schwerin", 95818, 733);
        City rostrock = new City("Rostrock", 208886, 1153);

        // Sachsen-Anhalt
        City magdeburg = new City("Magdeburg", 238697, 1173);
        City halle     = new City("halle", 237865, 1762);

        City[] citiesOfBayern                   = {ingolstadt, munchen, nurnberg};
        City[] citiesOfThuringen                = {erfurt, jena};
        City[] citiesOfHessen                   = {frankfurt, wiesbaden};
        City[] citiesOfBadenwurttemberg         = {stuttgart, mannheim};
        City[] citiesOfSachsen                  = {dresden, leipzig};
        City[] citiesOfNiedersachsen            = {hannover, braunschweig};
        City[] citiesOfRheinlandpfalz           = {mainz, ludwigshafen};
        City[] citiesOfSchleswigholstein        = {kiel, luebeck};
        City[] citiesOfSaarland                 = {saarbruecken, neunkirchen};
        City[] citiesOfBerlin                   = {berlinCity};
        City[] citiesOfBrandenburg              = {potsdam, cottbus};
        City[] citiesOfBremen                   = {bremenCity};
        City[] citiesOfNordrheinwestfahlen      = {duesseldorf, koeln};
        City[] citiesOfHamburg                  = {hamburgCity};
        City[] citiesOfMecklenburgvorpommern    = {schwerin, rostrock};
        City[] citiesOfSachsenanhalt            = {magdeburg, halle};

        State thuringen             = new State("Thüringen");
        State bayern                = new State("Bayern");
        State hessen                = new State("Hessen");
        State badenWurttemberg      = new State("Baden-Württemberg");
        State sachsen               = new State("Sachsen");
        State niedersachsen         = new State("Niedersachsen");
        State rheinlandpfalz        = new State("Rheinland-Pfalz");
        State schleswigholstein     = new State("Schleswig-Holstein");
        State saarland              = new State("Saarland");
        State berlin                = new State("Berlin");
        State brandenburg           = new State("Brandenburg");
        State bremen                = new State("Bremen");
        State nordrheinwestfahlen   = new State("Nordrhein-Westfahlen");
        State hamburg               = new State("Hamburg");
        State mecklenburgvorpommern = new State("Mecklenburg-Vorpommern");
        State sachsenanhalt         = new State("Sachsen-Anhalt");

        State[] states = {thuringen, bayern, hessen, badenWurttemberg, sachsen, niedersachsen, rheinlandpfalz, schleswigholstein,
                saarland, berlin, brandenburg, bremen, nordrheinwestfahlen, hamburg, mecklenburgvorpommern, sachsenanhalt};

        // Städte setzten
        thuringen.setCities(citiesOfThuringen);
        bayern.setCities(citiesOfBayern);
        hessen.setCities(citiesOfHessen);
        badenWurttemberg.setCities(citiesOfBadenwurttemberg);
        sachsen.setCities(citiesOfSachsen);
        niedersachsen.setCities(citiesOfNiedersachsen);
        rheinlandpfalz.setCities(citiesOfRheinlandpfalz);
        schleswigholstein.setCities(citiesOfSchleswigholstein);
        saarland.setCities(citiesOfSaarland);
        berlin.setCities(citiesOfBerlin);
        brandenburg.setCities(citiesOfBrandenburg);
        bremen.setCities(citiesOfBremen);
        nordrheinwestfahlen.setCities(citiesOfNordrheinwestfahlen);
        hamburg.setCities(citiesOfHamburg);
        mecklenburgvorpommern.setCities(citiesOfMecklenburgvorpommern);
        sachsenanhalt.setCities(citiesOfSachsenanhalt);

        Country germany = new Country("Deutschland");
        germany.setStates(states);

        System.out.println("Erfolgreich gestartet");
        System.out.println("Geladenes Land: " + germany.getName());
        System.out.println(germany.getAmountOfStates() + " Bundesländer");
        System.out.println(citiesOfBayern.length + " Städte vom Bundesland " + bayern.getName());

        // set days precisely
        ingolstadt.setHistoryDay(1, 100);
        ingolstadt.setHistoryDay(2, 200);
        ingolstadt.setHistoryDay(3, 300);
        ingolstadt.setHistoryDay(4, 400);
        ingolstadt.setHistoryDay(5, 500);
        ingolstadt.setHistoryDay(6, 600);
        ingolstadt.setHistoryDay(7, 700);

        ingolstadt.reloadCity();

        munchen.addNewEntryToHistory(10);
        munchen.addNewEntryToHistory(100);
        munchen.addNewEntryToHistory(500);

        munchen.reloadCity();



        // growth algorithm for 1 year
        for (int day = 0; day < 365; day++){
            // TODO check how long these virusses were prevailing
            // change virus after the 100th day
            if (day > 100) stuttgart.setCurrentVirus(beta);
            stuttgart.addNewEntryToHistory(day * (day * 2));
            stuttgart.reloadCity();
        }


        System.out.println("Beende Programm");
    }
}

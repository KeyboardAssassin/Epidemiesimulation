package com.codewithdani.util;

import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import com.codewithdani.models.regional.State;
import com.codewithdani.models.threats.Virus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;

public class SimulationJsonHandler {

    private static final String PATH = "/Users/Dani/Desktop/germany.json";


    public static Country importCountryFromJson(String countryName) {
        Gson gson = new Gson();
        File filePath = new File(Paths.get(countryName + ".json").toString());
        Country country;

        try {
            FileReader reader = new FileReader(filePath);
            country = gson.fromJson(reader, Country.class);
            for (State state : country.getStates()) {
                for (City city : state.getCities()) {
                    city.createInfectionData();
                }
            }
            reader.close();

            return country;
        }
        catch (Exception e){
            System.out.println("Fehler beim importieren aus einer json Datei!");
            System.out.println("Erstelle Deutschland aus der Vorlage!");
        }

        return createPreExistingGermany();
    }

    public void exportCountryToJson(Country country){
        System.out.println("Überprüfe ob eine json Datei existiert..");

        File filePath = new File(PATH);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer;
        try {
            writer = new FileWriter(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            gson.toJson(country, writer);
            writer.flush();
            writer.close();
        }catch (Exception e){
            System.out.println("Fehler beim exportieren des Landes " + country.getName() + " in eine json Datei!");
        }
    }

    public static Country createPreExistingGermany() {
        File filePath = new File(Paths.get("germany.json").toString());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer;

        // Cities
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
        City rostock = new City("Rostock", 208886, 1153);

        // Sachsen-Anhalt
        City halle     = new City("Halle", 237865, 1762);
        City magdeburg = new City("Magdeburg", 238697, 1173);

        List<City> citiesOfBayern                   = List.of(ingolstadt, munchen, nurnberg);
        List<City> citiesOfThuringen                = List.of(erfurt, jena);
        List<City> citiesOfHessen                   = List.of(frankfurt, wiesbaden);
        List<City> citiesOfBadenwurttemberg         = List.of(stuttgart, mannheim);
        List<City> citiesOfSachsen                  = List.of(dresden, leipzig);
        List<City> citiesOfNiedersachsen            = List.of(hannover, braunschweig);
        List<City> citiesOfRheinlandpfalz           = List.of(mainz, ludwigshafen);
        List<City> citiesOfSchleswigholstein        = List.of(kiel, luebeck);
        List<City> citiesOfSaarland                 = List.of(saarbruecken, neunkirchen);
        List<City> citiesOfBerlin                   = List.of(berlinCity);
        List<City> citiesOfBrandenburg              = List.of(potsdam, cottbus);
        List<City> citiesOfBremen                   = List.of(bremenCity);
        List<City> citiesOfNordrheinwestfalen       = List.of(duesseldorf, koeln);
        List<City> citiesOfHamburg                  = List.of(hamburgCity);
        List<City> citiesOfMecklenburgvorpommern    = List.of(schwerin, rostock);
        List<City> citiesOfSachsenanhalt            = List.of(magdeburg, halle);

        State bayern                = new State("Bayern", citiesOfBayern);
        State thuringen             = new State("Thüringen", citiesOfThuringen);
        State hessen                = new State("Hessen", citiesOfHessen);
        State badenWurttemberg      = new State("Baden-Württemberg", citiesOfBadenwurttemberg);
        State sachsen               = new State("Sachsen", citiesOfSachsen);
        State niedersachsen         = new State("Niedersachsen", citiesOfNiedersachsen);
        State rheinlandpfalz        = new State("Rheinland-Pfalz", citiesOfRheinlandpfalz);
        State schleswigholstein     = new State("Schleswig-Holstein", citiesOfSchleswigholstein);
        State saarland              = new State("Saarland", citiesOfSaarland);
        State berlin                = new State("Berlin", citiesOfBerlin);
        State brandenburg           = new State("Brandenburg", citiesOfBrandenburg);
        State bremen                = new State("Bremen", citiesOfBremen);
        State nordrheinwestfahlen   = new State("Nordrhein-Westfalen", citiesOfNordrheinwestfalen);
        State hamburg               = new State("Hamburg", citiesOfHamburg);
        State mecklenburgvorpommern = new State("Mecklenburg-Vorpommern", citiesOfMecklenburgvorpommern);
        State sachsenanhalt         = new State("Sachsen-Anhalt", citiesOfSachsenanhalt);


        State[] germanStates = {thuringen, bayern, hessen, badenWurttemberg, sachsen, niedersachsen, rheinlandpfalz, schleswigholstein,
                saarland, berlin, brandenburg, bremen, nordrheinwestfahlen, hamburg, mecklenburgvorpommern, sachsenanhalt};

        Country germany = new Country("Deutschland");
        germany.setStates(germanStates);

        try{
            writer = new FileWriter(filePath);
            gson.toJson(germany, writer);
            writer.flush();
            writer.close();
        }catch (Exception e){
            System.out.println("Fehler beim exportieren des Landes " + germany.getName() + " in eine json Datei!");
        }

        return germany;
    }

    public static boolean checkIfCountryJsonExists(String countryName){
        File file = new File(Paths.get(countryName + ".json").toString());
        return file.exists();
    }
}

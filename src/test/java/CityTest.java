import com.codewithdani.models.regional.City;
import com.codewithdani.models.regional.Country;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityTest {

    @Test
    public void settingAndGettingHistoryDatesTest(){
        City testCity = new City("Testcity", 100000, 100);

        // set days precisely
        testCity.setHistoryDay(1, 100);
        testCity.setHistoryDay(2, 200);
        testCity.setHistoryDay(3, 300);
        testCity.setHistoryDay(4, 400);
        testCity.setHistoryDay(5, 500);
        testCity.setHistoryDay(6, 600);
        testCity.setHistoryDay(7, 700);

        testCity.reloadCity();

        assertEquals(100, testCity.getEntryFromHistory(1));
        assertEquals(200, testCity.getEntryFromHistory(2));
        assertEquals(300, testCity.getEntryFromHistory(3));
        assertEquals(400, testCity.getEntryFromHistory(4));
        assertEquals(500, testCity.getEntryFromHistory(5));
        assertEquals(600, testCity.getEntryFromHistory(6));
        assertEquals(700, testCity.getEntryFromHistory(7));
    }

    @Test
    void addingNewDataToHistoryTest() {
        Country testCountry = new Country("Testcountry");
        City testCity = new City("Testcity", 100000, 100);

        testCity.addNewEntryToHistory(10, testCountry);
        testCity.addNewEntryToHistory(100, testCountry);
        testCity.addNewEntryToHistory(500, testCountry);

        testCity.reloadCity();

        assertEquals(10, testCity.getEntryFromHistory(1));
        assertEquals(100, testCity.getEntryFromHistory(2));
        assertEquals(500, testCity.getEntryFromHistory(3));
    }
}

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static testUtils.FilterTestUtils.*;

public class FilterTest {
    static FilterBot filterBot;
    static ArrayList<String> prices;
    static ArrayList<String> locations;
    static ArrayList<String> dates;

    @BeforeEach
    public void setUp() {
        filterBot = new FilterBot("https://www.letgo.com/arama?isSearchCall=true");
        filterBot.connect();
        filterBot.removeCookieOption();
    }

    @ParameterizedTest
    @ValueSource(strings = {"İzmir","Ankara","Antalya"})
    public void filterWithOnlyLocation(String location){
        filterBot.setLocation(location);
        filterBot.populate();
        filterBot.storeSearchData();

        locations = filterBot.getLocations();
        assertTrue(checkLocation(locations,location));
    }

    @ParameterizedTest
    @CsvSource({
            "100,2000",
            "1500,10000",
            "500,550"
    })
    public void filterWithOnlyPriceRange(String min, String max){
        filterBot.setPriceFilter(min,max);
        filterBot.populate();
        filterBot.storeSearchData();

        prices = filterBot.getPrices();
        assertTrue(checkPriceRange(prices,min,max));
    }

    @ParameterizedTest
    @CsvSource({
        "Son 24 saat" ,
        "Son 3 gün içinde",
        "Son 7 gün içinde",
        "Son 15 gün içinde"
    })
    public void filterWithOnlyDate(String date){
        filterBot.setDate(date);
        filterBot.populate();
        filterBot.storeSearchData();

        dates = filterBot.getDates();
        assertTrue(checkDate(dates,date));
    }

    @ParameterizedTest
    @CsvSource({
           "İzmir,1000,6600",
           "İstanbul,2000,2500",
           "Ankara,100,1000"
    })
    public void filterWithLocationAndPrice(String location, String min, String max){
        filterBot.setLocation(location);
        filterBot.setPriceFilter(min,max);
        filterBot.populate();
        filterBot.storeSearchData();

        locations = filterBot.getLocations();
        prices = filterBot.getPrices();

        assertTrue(checkLocationAndPriceRange(locations,prices,location,min,max));
    }


    @ParameterizedTest
    @CsvSource({
            "İzmir,Son 24 saat",
            "Ankara,Son 3 gün içinde",
            "Bursa,Son 7 gün içinde",
            "Çanakkale,Son 15 gün içinde"
    })
    public void filterWithLocationAndDate(String location, String date){
        filterBot.setLocation(location);
        filterBot.setDate(date);
        filterBot.populate();
        filterBot.storeSearchData();

        locations = filterBot.getLocations();
        dates = filterBot.getDates();

        assertTrue(checkLocationAndDate(locations,dates,location,date));
    }

    @ParameterizedTest
    @CsvSource({
            "Son 24 saat,100,2000",
            "Son 3 gün içinde,350,10000",
            "Son 7 gün içinde,0,999",
            "Son 15 gün içinde,105,2666",
    })
    public void filterWithDateAndPrice(String date, String min, String max){
        filterBot.setDate(date);
        filterBot.setPriceFilter(min,max);
        filterBot.populate();
        filterBot.storeSearchData();

        dates = filterBot.getDates();
        prices = filterBot.getPrices();

        assertTrue(checkDateAndPriceRange(dates,prices,date,min,max));
    }

    @ParameterizedTest
    @CsvSource({
            "Son 24 saat,100,2000,İzmir",
            "Son 3 gün içinde,350,10000,Antalya",
            "Son 7 gün içinde,0,999,Balıkesir",
            "Son 15 gün içinde,105,2666,İstanbul",
    })
    public void filterWithDateAndPriceAndLocation(String date, String min, String max,String location){
        filterBot.setDate(date);
        filterBot.setPriceFilter(min,max);
        filterBot.setLocation(location);
        filterBot.populate();
        filterBot.storeSearchData();

        dates = filterBot.getDates();
        prices = filterBot.getPrices();
        locations = filterBot.getLocations();

        assertTrue(checkLocationAndPriceRangeAndDate(locations,prices,dates,location,min,max,date));
    }

    @Test
    public void noFilteringAtAll(){
        filterBot.setLocation("");
        filterBot.setDate("");
        filterBot.setPriceFilter("0","0");
        filterBot.populate();
        filterBot.storeSearchData();

        locations = filterBot.getLocations();
        prices = filterBot.getPrices();
        dates = filterBot.getDates();

        assertFalse(locations.isEmpty() || prices.isEmpty() || dates.isEmpty());
    }

    @AfterAll
    public static void tearDown() {
        DriverFactory.quitDriver();
    }

}

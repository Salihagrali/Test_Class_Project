import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static testUtils.FilterTestUtils.checkOnlyLocation;

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
        assertTrue(checkOnlyLocation(locations,location));
    }
}

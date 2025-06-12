import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchPageTest {
    static SearchPage searchPage;
    static ArrayList<String> titleList;


    @BeforeEach
    public void setUp() {
        searchPage = new SearchPage("https://www.letgo.com/");
        searchPage.connect();
        searchPage.removeCookieOption();
    }

    //Equivalence Classes
    @Test
    public void searchWithValidKeyword() {
        searchPage.searchFor("araba");
        searchPage.populate();
        searchPage.storeSearchData();

        titleList = searchPage.getTitles();
        System.out.println(titleList.size());


        assertFalse(titleList.isEmpty());

    }

    @ParameterizedTest
    @ValueSource(strings = {"$$$$$>>£"," ","asdasvdsvadsafdsafsdafdassvsda"})
    public void searchWithInvalidKeyword(String keyword){
        searchPage.searchFor(keyword);
        assertTrue(searchPage.getNoResult().isDisplayed());
    }


    //Boundary Analaysis
    @ParameterizedTest
    @CsvSource({
            "0, 1000",
            "1, 1000",
            "100, 99999999999999999999",
            "100, 100000000000000000000"
    })
    public void testValidPrice(String minPrice, String maxPrice) {
        searchPage.searchFor("araba");
        searchPage.setPriceFilter(minPrice,maxPrice);

        searchPage.storeSearchData();

        titleList = searchPage.getTitles();
        assertFalse(titleList.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 1000, min",
            "100, 100000000000000000001, max"
    })
    public void testInvalidInputsAreNotAccepted(String min, String max, String fieldToCheck) {
        searchPage.searchFor("araba");
        searchPage.setPriceFilter(min, max);

        String expected = fieldToCheck.equals("min") ? min : max;
        String actual = searchPage.getPriceInputValue(fieldToCheck);

        assertNotEquals(expected, actual,
                fieldToCheck + " değeri sınır dışı olmasına rağmen değişmedi!");
    }


    @AfterAll
    public static void tearDown() {
        DriverFactory.quitDriver();
    }
}
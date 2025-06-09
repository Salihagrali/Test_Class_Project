package testUtils;

import java.util.ArrayList;

public class FilterTestUtils {

    public static boolean checkOnlyLocation(ArrayList<String> locations, String location){
        return locations.stream()
                .allMatch(loc -> loc.equalsIgnoreCase(location));
    }
}

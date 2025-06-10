package testUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class FilterTestUtils {

    public static boolean checkLocationAndPriceRangeAndDate(ArrayList<String> locations
            ,ArrayList<String> prices
            ,ArrayList<String> dates
            ,String location
            ,String min
            ,String max
            ,String date){
        return checkLocationAndPriceRange(locations,prices,location,min,max) && checkDate(dates,date);
    }

    public static boolean checkLocationAndPriceRange(ArrayList<String> locations
            ,ArrayList<String> prices
            ,String location
            ,String min
            ,String max){
        return checkLocation(locations,location) && checkPriceRange(prices, min, max);
    }

    public static boolean checkLocationAndDate(ArrayList<String> locations
            ,ArrayList<String> dates
            ,String location
            ,String date){
        return checkLocation(locations,location) && checkDate(dates,date);
    }

    public static boolean checkDateAndPriceRange(ArrayList<String> dates
            ,ArrayList<String> prices
            ,String date
            ,String min
            ,String max){
        return checkDate(dates,date) && checkPriceRange(prices,min,max);
    }

    public static boolean checkLocation(ArrayList<String> locations, String location){
        return locations.stream()
                .allMatch(loc -> loc.equalsIgnoreCase(location));
    }

    public static boolean checkPriceRange(ArrayList<String> prices, String min, String max){
        return prices
                .stream()
                .noneMatch(p -> {
                    String value = p.split(" ")[0];
                    if(value.contains(".")){
                        String[] arr = value.split("\\.");
                        value = arr[0] + arr[1];
                    }
                    int n = Integer.parseInt(value);
                    return n < Integer.parseInt(min) || n > Integer.parseInt(max);
                });
    }

    public static boolean checkDate(ArrayList<String> dates, String date){
        return dates.stream().allMatch(d -> isWithinSpecifiedDate(d,date));
    }

    private static boolean isWithinSpecifiedDate(String d, String range) {
        LocalDate now = LocalDate.now();
        LocalDate earliest;

        range = range.toUpperCase(Locale.forLanguageTag("tr"));

        switch (range) {
            case "BUGÜN":
                earliest = now;
                break;
            case "SON 24 SAAT":
                earliest = now.minusDays(1);
                break;
            case "SON 3 GÜN İÇİNDE":
                earliest = now.minusDays(3);
                break;
            case "SON 7 GÜN İÇİNDE":
                earliest = now.minusDays(7);
                break;
            case "SON 15 GÜN İÇİNDE":
                earliest = now.minusDays(15);
                break;
            default:
                throw new IllegalArgumentException("Unsupported range: " + range);
        }

        LocalDate parsedDate = parseToLocalDate(d);

        if (parsedDate == null) return false;

        return !parsedDate.isBefore(earliest);
    }

    private static LocalDate parseToLocalDate(String input) {
        input = input.toUpperCase(Locale.forLanguageTag("tr")).trim();
        LocalDate now = LocalDate.now();

        try {
            if (input.equals("BUGÜN")) return now;
            if (input.equals("DÜN")) return now.minusDays(1);
            if (input.matches("\\d+ GÜN ÖNCE")) {
                int days = Integer.parseInt(input.split(" ")[0]);
                return now.minusDays(days);
            }

            return parseTurkishDate(input);
        } catch (Exception e) {
            System.out.println("Could not parse date: " + input);
            return null;
        }
    }

    private static LocalDate parseTurkishDate(String input) {
        Map<String, Integer> turkishMonths = Map.ofEntries(
                Map.entry("OCAK", 1),
                Map.entry("ŞUBAT", 2),
                Map.entry("MART", 3),
                Map.entry("NİSAN", 4),
                Map.entry("MAYIS", 5),
                Map.entry("HAZIRAN", 6),
                Map.entry("TEMMUZ", 7),
                Map.entry("AĞUSTOS", 8),
                Map.entry("EYLÜL", 9),
                Map.entry("EKİM", 10),
                Map.entry("KASIM", 11),
                Map.entry("ARALIK", 12)
        );

        String[] parts = input.split(" ");
        int day = Integer.parseInt(parts[0]);
        int month = turkishMonths.getOrDefault(parts[1], -1);

        if (month == -1) throw new IllegalArgumentException("Unknown month: " + parts[1]);

        LocalDate now = LocalDate.now();
        return LocalDate.of(now.getYear(), month, day);
    }
}

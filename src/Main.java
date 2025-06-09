public class Main {
    public static void main(String[] args) {
        FilterBot filterBot = new FilterBot("https://www.letgo.com/arama?isSearchCall=true");
        filterBot.connect();
        filterBot.removeCookieOption();
        filterBot.setLocation("İzmir");
        filterBot.setPriceFilter("100","5000");
        filterBot.setDate("Son 3 gün içinde");
        filterBot.populate();
        filterBot.storeSearchData();
        System.out.println(filterBot.getPrices());
        System.out.println(filterBot.getLocations());
        System.out.println(filterBot.getDates());

        boolean check = filterBot.getPrices()
                .stream()
                .anyMatch(p -> {
                    String value = p.split(" ")[0];
                    if(value.contains(".")){
                        String[] arr = value.split("\\.");
                        value = arr[0] + arr[1];
                    }
            int n = Integer.parseInt(value);
            return n < 100 || n > 5000;
        });

        System.out.println("Boolean: " + check);
    }
}

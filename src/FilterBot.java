import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FilterBot {
    private WebDriver driver;
    private String url;
    private final int TIMEOUT = 4;
    private WebDriverWait wait;
    private Actions actionProvider;

    private ArrayList<String> prices;
    private ArrayList<String> locations;
    private ArrayList<String> dates;

    public FilterBot(String url){
        this.url = url;
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        this.actionProvider = new Actions(driver);
        this.prices = new ArrayList<>();
        this.locations = new ArrayList<>();
        this.dates = new ArrayList<>();
    }


    public void connect(){
        driver.get(url);
    }

    public void searchFor(String query) {
        String searchXPath = "/html/body/header/div/div[2]/div/div[2]/div[1]/div/input";
        WebElement searchInput = wait.until(driver -> driver.findElement(By.xpath(searchXPath)));
        searchInput.clear();

        searchInput.sendKeys(query);

        actionProvider.sendKeys(Keys.ENTER).perform();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
    }

    public void setLocation(String cityName){
        if(cityName.isEmpty()){
            actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        }else {
            String locationXPath = "/html/body/div[2]/main/div/div[2]/aside/div/section/div/div[2]/div/div/div";
            String locationInputXPath = "/html/body/div[2]/main/div/div[2]/aside/div/section/div/div[2]/div/div/div[2]/div/div/input";
            String selectLocationXPath = "/html/body/div[2]/main/div/div[2]/aside/div/section/div/div[2]/div/div/div[2]/div/div[2]/div";
            String resetWindowXPath = "/html/body/div[2]/main/div/div/h1";

            WebElement locationElement = wait.until(driver -> driver.findElement(By.xpath(locationXPath)));
            locationElement.click();

            WebElement locationInputElement = wait.until(driver -> driver.findElement(By.xpath(locationInputXPath)));
            locationInputElement.clear();
            locationInputElement.sendKeys(cityName);

            WebElement selectLocationElement = wait.until(driver -> driver.findElement(By.xpath(selectLocationXPath)));
            selectLocationElement.click();

            WebElement resetElement = wait.until(driver -> driver.findElement(By.xpath(resetWindowXPath)));
            resetElement.click();

            actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
        }
    }

    public void setPriceFilter (String min, String max) {
        String minPriceXPath = "/html/body/div[2]/main/div/div[2]/aside/div[2]/div[3]/div[2]/div/div[1]/input";
        String maxPriceXPath = "/html/body/div[2]/main/div/div[2]/aside/div[2]/div[3]/div[2]/div/div[3]/input";
        String applyButtonXPath = "/html/body/div[2]/main/div/div[2]/aside/div[3]/button[2]";
        String resetWindowXPath = "/html/body/div[2]/main/div/div/h1";

        WebElement minInput = wait.until(driver -> driver.findElement(By.xpath(minPriceXPath)));
        WebElement maxInput = wait.until(driver -> driver.findElement(By.xpath(maxPriceXPath)));
        WebElement applyButton = wait.until(driver -> driver.findElement(By.xpath(applyButtonXPath)));
        WebElement resetElement = wait.until(driver -> driver.findElement(By.xpath(resetWindowXPath)));

        minInput.clear();
        minInput.sendKeys(min);
        maxInput.clear();
        maxInput.sendKeys(max);
        applyButton.click();
        resetElement.click();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
    }

    public void setDate(String date){
        String applyButtonXPath = "/html/body/div[2]/main/div/div[2]/aside/div[3]/button[2]";
        String dateXPath = "/html/body/div[2]/main/div/div[2]/aside/div[2]/div[4]/div[2]/div/div/";
        String resetWindowXPath = "/html/body/div[2]/main/div/div/h1";
        dateXPath = switch (date) {
            case "Son 24 saat" -> dateXPath.concat("div[2]");
            case "Son 3 gün içinde" -> dateXPath.concat("div[3]");
            case "Son 7 gün içinde" -> dateXPath.concat("div[4]");
            case "Son 15 gün içinde" -> dateXPath.concat("div[5]");
            default -> dateXPath.concat("div"); // Tümü için
        };
        if(date.equals("Son 15 gün içinde")){
            populate();
        }
        String finalDateXPath = dateXPath;
        WebElement dateElement = wait.until(driver -> driver.findElement(By.xpath(finalDateXPath)));
        WebElement applyButton = wait.until(driver -> driver.findElement(By.xpath(applyButtonXPath)));

        dateElement.click();
        applyButton.click();
        WebElement resetElement = wait.until(driver -> driver.findElement(By.xpath(resetWindowXPath)));
//        resetElement.click();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
    }

    public void storeSearchData() {
        prices.clear();
        locations.clear();
        dates.clear();

        String listParentClassName = ".search-result-items";
        String itemPriceClass = ".item-price";
        String itemLocationClass = ".location-city";
        String itemDateClass = ".history";

        try {
            WebElement parentDiv = wait.until(driver -> driver.findElement(By.cssSelector(listParentClassName)));
            List<WebElement> priceElements = parentDiv.findElements(By.cssSelector(".item-price > span"));
            List<WebElement> locationElements = parentDiv.findElements(By.cssSelector(itemLocationClass));
            List<WebElement> dateElements = parentDiv.findElements(By.cssSelector(itemDateClass));
            priceElements.forEach(p -> prices.add(p.getText()));
            locationElements.forEach(l -> locations.add(l.getText().split(",")[0]));
            dateElements.forEach(d -> dates.add(d.getText()));

        } catch (Exception e) {
            System.out.println("Titles could not be stored.: " + e.getMessage());
        }
    }


    public void removeCookieOption() {
        String buttonXPath = "/html/body/div[3]/div[2]";
        try {
            WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(buttonXPath)));
            WebElement clickableButton = wait.until(ExpectedConditions.elementToBeClickable(button));
            clickableButton.click();
        } catch (Exception e) {
            System.out.println("it has already done.");
        }
    }

    public void populate(){
        actionProvider.sendKeys(Keys.END).perform();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
    }

    public ArrayList<String> getPrices() {
        return prices;
    }
    public ArrayList<String> getDates() {
        return dates;
    }
    public ArrayList<String> getLocations() {
        return locations;
    }

    public WebElement getNoResult(){
        WebElement noResult = driver.findElement(By.className("no-results-container"));
        return noResult;
    }


    public String getPriceInputValue(String which) {
        String xpath;
        if (which.equals("min")) {
            xpath = "/html/body/div[2]/main/div/div[2]/aside/div[3]/div[4]/div[2]/div/div[1]/input";
        } else {
            xpath = "/html/body/div[2]/main/div/div[2]/aside/div[3]/div[4]/div[2]/div/div[3]/input";
        }

        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        return input.getAttribute("value");
    }

}

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class SearchPage {
    private WebDriver driver;
    private String url;
    private final int TIMEOUT = 4;
    private WebDriverWait wait;
    private Actions actionProvider;

    private ArrayList<String> titles;

    public SearchPage(String url){

        this.url = url;
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        this.actionProvider = new Actions(driver);
        this.titles = new ArrayList<>();
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



    public void setPriceFilter (String min, String max) {
        String minPriceXPath = "/html/body/div[2]/main/div/div[2]/aside/div[3]/div[4]/div[2]/div/div[1]/input";
        String maxPriceXPath = "/html/body/div[2]/main/div/div[2]/aside/div[3]/div[4]/div[2]/div/div[3]/input";
        String applyButtonXPath = "/html/body/div[2]/main/div/div[2]/aside/div[4]/button[2]";
       // String clearButtonXPath = "/html/body/div[2]/main/div/div[2]/aside/div[4]/button[1]";

        WebElement minInput = wait.until(driver -> driver.findElement(By.xpath(minPriceXPath)));
        WebElement maxInput = wait.until(driver -> driver.findElement(By.xpath(maxPriceXPath)));
        WebElement applyButton = wait.until(driver -> driver.findElement(By.xpath(applyButtonXPath)));

        minInput.clear();
        minInput.sendKeys(min);
        maxInput.clear();
        maxInput.sendKeys(max);
        applyButton.click();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();


    }

    //Adds the titles of the ads into a list.
    public void storeSearchData() {
        titles.clear();

        String listParentClassName = ".search-result-items";
        String itemTitleClass = ".item-title";

        try {
            WebElement parentDiv = wait.until(driver -> driver.findElement(By.cssSelector(listParentClassName)));
            List<WebElement> titleElements = parentDiv.findElements(By.cssSelector(itemTitleClass));

            for (WebElement titleElement : titleElements) {
                String titleText = titleElement.getText().trim();
                if (!titleText.isEmpty()) {
                    titles.add(titleText);
                }
            }

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

    //goes to the bottom of the page
    public void populate(){
        actionProvider.sendKeys(Keys.END).perform();
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();
    }

    public ArrayList<String> getTitles() {
        return titles;
    }



    public WebElement getNoResult(){
        WebElement noResult = driver.findElement(By.className("no-results-container"));
        return noResult;
    }



    //It checks whether the number we entered instead of input has changed after searching.
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

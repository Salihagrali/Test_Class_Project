import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

public class LoginBot {
    private WebDriver driver;
    private String url;
    private final int TIMEOUT = 3;
    private WebDriverWait wait;

    private Actions actionProvider;

    public LoginBot(String url) {
        this.url = url;
        this.driver = DriverFactory.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT));
        this.actionProvider = new Actions(driver);
    }


    public void connect(){
        driver.get(url);
    }
    private void goToLoginPage(){
        String loginButtonXPath = "/html/body/header/div/div[2]/div/div[3]/button";
        WebElement button = wait.until(driver -> driver.findElement(By.xpath(loginButtonXPath)));
        WebElement clickableButton = wait.until(ExpectedConditions.elementToBeClickable(button));
        clickableButton.click();
    }

    private void goToMailButton(){
        String mailButtonXPath = "/html/body/div[3]/div[2]/div/div[2]/button[2]";
        WebElement button = wait.until(driver -> driver.findElement(By.xpath(mailButtonXPath)));
        WebElement clickableButton = wait.until(ExpectedConditions.elementToBeClickable(button));
        clickableButton.click();
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
    private void enterEmail(String email){
        String emailInputXPath = "/html/body/div[3]/div[2]/div[3]/div/span/div/input";
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(emailInputXPath)));
        emailInput.clear();
        emailInput.sendKeys(email);

    }
    public boolean isContinueButtonEnabled() {
        WebElement continueButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("btnContinueAuth")));


        String ariaDisabled = continueButton.getAttribute("aria-disabled");
        return "false".equals(ariaDisabled);
    }


    public void login(String emailInput){

        goToLoginPage();
        goToMailButton();
        enterEmail(emailInput);
        actionProvider.pause(Duration.ofSeconds(TIMEOUT)).build().perform();

    }
}

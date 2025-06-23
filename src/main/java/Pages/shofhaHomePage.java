package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class shofhaHomePage extends pageBase{
    public shofhaHomePage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    //Define Elements
    @FindBy(id = "Sub_nav")
    WebElement subscriptionBtn;

    //Define Functions
    public void openSubscriptionPage(){
        try {
            wait.until(ExpectedConditions.visibilityOf(subscriptionBtn)).click();
            Thread.sleep(3000);
            System.out.println("Subscription page opened successfully");
            checkCurrentURL("https://shofha.com/payment/dcb/subscribe");
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }
}
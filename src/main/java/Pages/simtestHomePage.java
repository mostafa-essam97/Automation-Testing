package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class simtestHomePage extends pageBase{
    public simtestHomePage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    // Define Elements
    @FindBy(linkText = "Reservations")
    WebElement reservationsTap;

    @FindBy(id = "nav-item-testing")
    WebElement testingTap;

    @FindBy(id = "link-sms")
    WebElement smsTap;

    //Define Functions
    public void openReservations(){
        try {
            wait.until(ExpectedConditions.visibilityOf(reservationsTap)).click();
        }
        catch (Exception e){
            System.out.println("Something want wrong, Please retry. " + e.getMessage());
        }
    }

    public void openTestingTap(){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(testingTap)).click();
            System.out.println("Testing list opened successfully.");
            Thread.sleep(5000);
        }
        catch (Exception e){
            System.out.println("Something want wrong, Please retry. " + e.getMessage());
            Assert.fail("Can't open the testing tap.");
        }
    }

    public void openSMS(){
        try {
            wait.until(ExpectedConditions.visibilityOf(smsTap)).click();
            System.out.println("SMS page opened successfully.");
        }
        catch (Exception e){
            System.out.println("Something want wrong, Please retry. " + e.getMessage());
            Assert.fail("Can't open the SMS page." + e.getMessage());
        }
    }
}
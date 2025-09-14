package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class shofhaSubscriptionPage extends pageBase{
    public shofhaSubscriptionPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    //Define Elements
    @FindBy(name = "countryCode")
    WebElement countriesMenu;

    @FindBy(id = "inputMail")
    WebElement phonefield;

    @FindBy (id = "nextbtn")
    WebElement nextBtn;

    @FindBy (css = "div.PopUp")
    WebElement activeNumberPopUp;

    //Define Functions
    public void selectCountryByCode(String countryCode) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(countriesMenu)).click();
            Select select = new Select(countriesMenu);
            select.selectByValue(countryCode);
            System.out.println("The country code has been chosen successfully: " + countryCode);
            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println("Failed to catch the country code: "+ countryCode+", Please select valid code." + e.getMessage() );
            Assert.fail("Failed to choose country code: " + countryCode + e.getMessage());
        }
    }

    public void enterReservedNumber(String reservedNumber) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(phonefield)).sendKeys(reservedNumber);
            System.out.println("The country number inserted successfully: " + reservedNumber);
        } catch (Exception e) {
            System.out.println("Failed to enter the reserved number from SIMTest: "+ reservedNumber+", Please try again." );
            Assert.fail("Failed to subscribe with this number: " + reservedNumber);
        }
    }

    public void accessNewLPPage(){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(nextBtn)).click();
            //checkNumberIsActive();
            Thread.sleep(3000);
            System.out.println("✅ Current page URL after click next: " + driver.getCurrentUrl());
        } catch (Exception e) {
            System.out.println("Can't click the next button the system remains in the subscription page. " + e.getMessage() );
            Assert.fail("Failed to navigate to the OTP page. " +e.getMessage());
        }
    }

    public void checkNumberIsActive(){
        try {
            wait.until(ExpectedConditions.visibilityOf(activeNumberPopUp));
            String popupText = activeNumberPopUp.getText().trim();
            if (popupText.contains("هذا الرقم مستخدم بالفعل")){
                System.out.println("Already Subscribed, Click the button below to login.\n" + popupText);
                Assert.fail("The inserted number is an active Number.");
            }
            else {
                System.out.println("Navigated to the Lp page successfully. ");
            }
        } catch (Exception e) {
            System.out.println("Something went wrong. " + e.getMessage());
        }
    }
}
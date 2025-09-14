package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class verifySubscriptionPage extends pageBase{
    public verifySubscriptionPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //Define Elements
    @FindBy(name = "code")
    WebElement pinCode;

    @FindBy(id = "verify_btn")
    WebElement verifyBtn;

    //Define Functions
    public void insertPinCode(String verifyCode){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(pinCode)).sendKeys(verifyCode);
            System.out.println("Pin Code inserted successfully. " + verifyCode);
        } catch (Exception e) {
            System.out.println("Something want wrong. " + e.getMessage());
            Assert.fail("Can't insert the OTP. "+ e.getMessage());
        }
    }


    public void accessVerifyBtn(){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(verifyBtn)).click();
            //Assert.assertEquals(verifyBtn.getText(),"تحقق");
            System.out.println("Verify button clicked successfully.\nCongratulations your are subscribed on Shofha enjoy watching.");
        } catch (Exception e) {
            System.out.println("Something want wrong. " + e.getMessage());
            Assert.fail("Can't insert the OTP. "+ e.getMessage());
        }
    }
}

package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class shofhaLP extends pageBase {
    public shofhaLP(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //Define Element
    @FindBy(id = "send_btn")
    WebElement nextToOTPBtn;

    @FindBy (xpath = "//h1[contains(text(),'اختر الباقة المناسبة لك')]")
    WebElement packagesTitle;

    //Define Functions
    public void navigateToPackegesPage() {
        String expectedPackageTitle = "اختر الباقة المناسبة لك واستمتع الآن بالمشاهدة!";
        try {
            wait.until(ExpectedConditions.visibilityOf(packagesTitle));
            Assert.assertEquals(packagesTitle.getText(),expectedPackageTitle);
            System.out.println("You are in the packages page, Please select one of the subscription packages..");
            wait.until(ExpectedConditions.elementToBeClickable(nextToOTPBtn)).click();
            System.out.println("The page title is: "+ packagesTitle.getText());
        } catch (Exception e) {
            System.out.println("Can't access the new LP page. " + e.getMessage());
            Assert.fail("Can't find the right page. " + e.getMessage());
        }
    }
}

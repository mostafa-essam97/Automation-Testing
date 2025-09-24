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
        wait = new WebDriverWait(driver, Duration.ofSeconds(15)); // خليها أطول شوية
    }

    // Define Elements
    @FindBy(id = "send_btn")
    WebElement nextToOTPBtn;

    // خلي النص العربي أكثر مرونة باستخدام normalize-space
    @FindBy(xpath = "//h1[contains(normalize-space(.),'اختر الباقة المناسبة لك')]")
    WebElement packagesTitle;

    // Define Functions
    public void navigateToPackegesPage() {
        String expectedPackageTitle = "اختر الباقة المناسبة لك واستمتع الآن بالمشاهدة!";
        try {
            // استنى لحد ما العنوان يبان
            wait.until(ExpectedConditions.visibilityOf(packagesTitle));

            // قارن النص بعد normalize-space للتأكد من المطابقة
            String actualTitle = packagesTitle.getText().trim();
            Assert.assertEquals(actualTitle, expectedPackageTitle,
                    "Page title doesn't match. Expected: " + expectedPackageTitle + " but found: " + actualTitle);

            System.out.println("✅ You are in the packages page, Please select one of the subscription packages..");

            // استنى زرار الـ OTP button يبقى clickable
            wait.until(ExpectedConditions.elementToBeClickable(nextToOTPBtn)).click();

            System.out.println("The page title is: " + actualTitle);
        } catch (Exception e) {
            System.out.println("❌ Can't access the new LP page. " + e.getMessage());
            Assert.fail("Can't find the right page. " + e.getMessage());
        }
    }
}
package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

/**
 * ShofhaLandingPage - Page Object for Shofha landing page (LP)
 * Previously named shofhaLP
 */
public class ShofhaLandingPage extends BasePage {

    public ShofhaLandingPage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(id = "send_btn")
    private WebElement nextToOTPBtn;

    @FindBy(xpath = "//h1[contains(normalize-space(.),'اختر الباقة المناسبة لك')]")
    private WebElement packagesTitle;

    // ============ Actions ============

    /**
     * Navigate to packages page and verify title
     */
    public void navigateToPackagesPage() {
        String expectedTitle = "اختر الباقة المناسبة لك واستمتع الآن بالمشاهدة!";
        
        try {
            logger.info("Waiting for packages page to load...");
            
            // Wait for title to be visible
            longWait.until(ExpectedConditions.visibilityOf(packagesTitle));
            
            // Verify title
            String actualTitle = packagesTitle.getText().trim();
            Assert.assertEquals(actualTitle, expectedTitle,
                    "Page title mismatch. Expected: " + expectedTitle + " but found: " + actualTitle);
            
            logger.info("✅ Packages page loaded successfully");
            logger.info("Page title: {}", actualTitle);
            
            // Wait for OTP button and click
            click(nextToOTPBtn);
            
            logger.info("✅ Clicked Next to OTP button");
        } catch (AssertionError ae) {
            logger.error("❌ Title verification failed: {}", ae.getMessage());
            throw ae;
        } catch (Exception e) {
            logger.error("❌ Failed to access packages page: {}", e.getMessage());
            Assert.fail("Failed to find packages page: " + e.getMessage());
        }
    }

    /**
     * Check if packages page is displayed
     */
    public boolean isPackagesPageDisplayed() {
        return isElementDisplayed(packagesTitle);
    }

    /**
     * Click the Next/OTP button
     */
    public void clickNextToOTP() {
        click(nextToOTPBtn);
        logger.info("Next to OTP button clicked");
    }
}






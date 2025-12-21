package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

/**
 * ShofhaLP - Page Object for Shofha Landing Page (LP)
 */
public class shofhaLP extends BasePage {

    public shofhaLP(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(id = "send_btn")
    private WebElement nextToOTPBtn;

    @FindBy(xpath = "//h1[contains(normalize-space(.),'اختر الباقة المناسبة لك')]")
    private WebElement packagesTitle;

    // ============ Actions ============

    /**
     * Navigate to packages page and click next
     */
    public void navigateToPackegesPage() {
        String expectedPackageTitle = "اختر الباقة المناسبة لك واستمتع الآن بالمشاهدة!";
        logger.info("Navigating to packages page...");

        try {
            // Wait for packages title to appear
            longWait.until(ExpectedConditions.visibilityOf(packagesTitle));

            String actualTitle = packagesTitle.getText().trim();
            logger.info("Page title found: {}", actualTitle);

            // Verify title
            if (!actualTitle.equals(expectedPackageTitle)) {
                logger.warn("Title mismatch - Expected: '{}', Actual: '{}'", expectedPackageTitle, actualTitle);
            }

            logger.info("✅ You are in the packages page");
            logger.info("   Please select one of the subscription packages...");

            // Click next button
            waitForClickable(nextToOTPBtn).click();
            logger.info("Clicked 'Next to OTP' button");

        } catch (Exception e) {
            logger.error("❌ Can't access the packages page: {}", e.getMessage());
            logger.error("   Possible causes:");
            logger.error("   1. Phone number not valid or not entered");
            logger.error("   2. Country code not selected");
            logger.error("   3. Landing page layout changed");
            logger.error("   4. Network/loading issues");
            Assert.fail("Cannot access packages page: " + e.getMessage());
        }
    }

    /**
     * Check if packages page is displayed
     */
    public boolean isPackagesPageDisplayed() {
        return isElementDisplayed(packagesTitle);
    }

    /**
     * Check if next button is visible
     */
    public boolean isNextButtonVisible() {
        return isElementDisplayed(nextToOTPBtn);
    }
}
package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

/**
 * VerifySubscriptionPage - Page Object for OTP verification
 */
public class verifySubscriptionPage extends BasePage {

    public verifySubscriptionPage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(name = "code")
    private WebElement pinCodeField;

    @FindBy(id = "verify_btn")
    private WebElement verifyBtn;

    // ============ Actions ============

    /**
     * Insert OTP/PIN code
     */
    public void insertPinCode(String verifyCode) {
        try {
            logger.info("Inserting PIN code...");
            
            sendKeys(pinCodeField, verifyCode);
            
            logger.info("PIN code inserted successfully");
        } catch (Exception e) {
            logger.error("Failed to insert PIN code: {}", e.getMessage());
            Assert.fail("Cannot insert OTP: " + e.getMessage());
        }
    }

    /**
     * Click verify button
     */
    public void clickVerifyButton() {
        try {
            logger.info("Clicking verify button...");
            
            click(verifyBtn);
            
            // Wait for verification to process
            waitForPageStability();
            
            logger.info("Verify button clicked successfully");
            logger.info("Congratulations! You are now subscribed to Shofha. Enjoy watching!");
        } catch (Exception e) {
            logger.error("Failed to click verify button: {}", e.getMessage());
            Assert.fail("Cannot verify OTP: " + e.getMessage());
        }
    }

    /**
     * Complete OTP verification (insert code and verify)
     */
    public void completeVerification(String otpCode) {
        insertPinCode(otpCode);
        clickVerifyButton();
    }

    /**
     * Check if verification page is displayed
     */
    public boolean isVerificationPageDisplayed() {
        return isElementDisplayed(pinCodeField) && isElementDisplayed(verifyBtn);
    }

    /**
     * Clear PIN code field
     */
    public void clearPinCode() {
        try {
            waitForClickable(pinCodeField).clear();
            logger.info("PIN code field cleared");
        } catch (Exception e) {
            logger.warn("Could not clear PIN code field: {}", e.getMessage());
        }
    }
}

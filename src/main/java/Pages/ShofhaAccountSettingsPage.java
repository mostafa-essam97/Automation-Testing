package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

/**
 * ShofhaAccountSettingsPage - Page Object for Shofha account settings
 */
public class ShofhaAccountSettingsPage extends BasePage {

    private final Actions actions;

    public ShofhaAccountSettingsPage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
    }

    // ============ Elements ============

    @FindBy(css = "button[data-target='#collapseThree']")
    private WebElement subscriptionDetailsBtn;

    @FindBy(xpath = "//button[@data-target='#cancelBilling']")
    private WebElement cancelSubBtn;

    @FindBy(className = "confirmation")
    private WebElement cancellationPopupScreen;

    @FindBy(xpath = "//p[contains(text(),'هل انت متأكد من الغاء الاشتراك؟')]")
    private WebElement popupCancelScreenTitle;

    @FindBy(className = "confirmYes")
    private WebElement yesBtn;

    // ============ Actions ============

    /**
     * Open subscription details section
     */
    public void openSubscriptionDetails() {
        try {
            logger.info("Opening subscription details...");
            
            scrollToElement(subscriptionDetailsBtn);
            click(subscriptionDetailsBtn);
            
            waitForPageStability();
            
            logger.info("✅ Subscription details section opened");
        } catch (Exception e) {
            logger.error("❌ Failed to open subscription details: {}", e.getMessage());
            Assert.fail("Cannot open subscription details: " + e.getMessage());
        }
    }

    /**
     * Click cancel subscription button
     */
    public void clickCancelSubscription() {
        try {
            logger.info("Clicking cancel subscription...");
            
            click(cancelSubBtn);
            
            // Verify popup appears
            wait.until(ExpectedConditions.visibilityOf(popupCancelScreenTitle));
            String popupText = popupCancelScreenTitle.getText();
            Assert.assertEquals(popupText, "هل انت متأكد من الغاء الاشتراك?",
                    "Unexpected popup text: " + popupText);
            
            logger.info("✅ Cancellation popup displayed");
        } catch (Exception e) {
            logger.error("❌ Failed to open cancellation popup: {}", e.getMessage());
            Assert.fail("Cannot open cancellation popup: " + e.getMessage());
        }
    }

    /**
     * Confirm the cancellation by clicking Yes
     */
    public void confirmCancellation() {
        try {
            logger.info("Confirming cancellation...");
            
            // Move to element and click
            actions.moveToElement(yesBtn).perform();
            click(yesBtn);
            
            // Wait for confirmation to process
            waitForPageStability();
            
            logger.info("✅ Cancellation confirmed successfully");
        } catch (Exception e) {
            logger.error("❌ Failed to confirm cancellation: {}", e.getMessage());
            Assert.fail("Cannot confirm cancellation: " + e.getMessage());
        }
    }

    /**
     * Complete the full cancellation flow
     */
    public void completeCancellation() {
        openSubscriptionDetails();
        clickCancelSubscription();
        confirmCancellation();
    }

    /**
     * Check if subscription details are visible
     */
    public boolean isSubscriptionDetailVisible() {
        return isElementDisplayed(subscriptionDetailsBtn);
    }

    /**
     * Check if cancel button is available
     */
    public boolean isCancelButtonAvailable() {
        return isElementDisplayed(cancelSubBtn);
    }
}






package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

/**
 * ShofhaAccountSettingPage - Page Object for Shofha account settings
 */
public class shofhaAccountSettingPage extends BasePage {
    private Actions actions;

    public shofhaAccountSettingPage(WebDriver driver) {
        super(driver);
        actions = new Actions(driver);
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
     * Access subscription details section
     */
    public void accessSubscriptionDetails() {
        logger.info("Opening subscription details section...");
        try {
            scrollToElement(subscriptionDetailsBtn);
            waitForClickable(subscriptionDetailsBtn).click();
            
            logger.info("✅ User subscription details section opened successfully");
        } catch (Exception e) {
            logger.error("❌ Can't open user subscription details: {}", e.getMessage());
            logger.error("   Possible causes:");
            logger.error("   1. User is not logged in to Shofha");
            logger.error("   2. User has no active subscription");
            logger.error("   3. Page layout has changed");
            Assert.fail("Cannot open user subscription details: " + e.getMessage());
        }
    }

    /**
     * Click cancel subscription button
     */
    public void CancelSubscription() {
        logger.info("Clicking cancel subscription button...");
        try {
            waitForClickable(cancelSubBtn).click();
            
            // Verify popup appears
            wait.until(ExpectedConditions.visibilityOf(popupCancelScreenTitle));
            String popupText = popupCancelScreenTitle.getText();
            
            Assert.assertEquals(popupText, "هل انت متأكد من الغاء الاشتراك؟",
                    "Unexpected popup text: " + popupText);
            
            logger.info("✅ Cancellation popup screen displayed correctly");
            
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("❌ Can't open cancellation popup: {}", e.getMessage());
            logger.error("   Possible causes:");
            logger.error("   1. Cancel button not visible/clickable");
            logger.error("   2. Subscription already cancelled");
            logger.error("   3. Page not fully loaded");
            Assert.fail("Cannot open cancellation popup screen: " + e.getMessage());
        }
    }

    /**
     * Confirm cancellation by clicking Yes button
     */
    public void confirmCancellation() {
        logger.info("Confirming cancellation...");
        try {
            actions.moveToElement(yesBtn).perform();
            waitForClickable(yesBtn).click();
            
            Thread.sleep(5000);
            
            logger.info("✅ Subscription cancellation confirmed successfully");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("❌ Can't click Yes button: {}", e.getMessage());
            logger.error("   Possible causes:");
            logger.error("   1. Button not visible in viewport");
            logger.error("   2. Popup overlay blocking the button");
            logger.error("   3. Button locator changed");
            Assert.fail("Cannot click Yes button to confirm cancellation: " + e.getMessage());
        }
    }

    /**
     * Check if subscription details button is visible
     */
    public boolean isSubscriptionDetailsVisible() {
        return isElementDisplayed(subscriptionDetailsBtn);
    }

    /**
     * Check if cancel button is visible
     */
    public boolean isCancelButtonVisible() {
        return isElementDisplayed(cancelSubBtn);
    }
}
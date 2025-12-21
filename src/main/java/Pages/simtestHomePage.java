package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

/**
 * SimtestHomePage - Page Object for SIMTest home/dashboard page
 */
public class simtestHomePage extends BasePage {

    public simtestHomePage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(linkText = "Reservations")
    private WebElement reservationsTab;

    @FindBy(id = "nav-item-testing")
    private WebElement testingTab;

    @FindBy(id = "link-sms")
    private WebElement smsTab;

    // ============ Actions ============

    /**
     * Open Reservations tab
     */
    public void openReservations() {
        try {
            logger.info("Opening Reservations tab...");
            click(reservationsTab);
            waitForPageStability();
            logger.info("Reservations tab opened");
        } catch (Exception e) {
            logger.error("Failed to open Reservations: {}", e.getMessage());
            Assert.fail("Cannot open Reservations tab: " + e.getMessage());
        }
    }

    /**
     * Open Testing tab
     */
    public void openTestingTab() {
        try {
            logger.info("Opening Testing tab...");
            click(testingTab);
            waitForPageStability();
            logger.info("Testing tab opened");
        } catch (Exception e) {
            logger.error("Failed to open Testing tab: {}", e.getMessage());
            Assert.fail("Cannot open Testing tab: " + e.getMessage());
        }
    }

    /**
     * Open SMS page
     */
    public void openSMS() {
        try {
            logger.info("Opening SMS page...");
            click(smsTab);
            waitForPageStability();
            logger.info("SMS page opened");
        } catch (Exception e) {
            logger.error("Failed to open SMS page: {}", e.getMessage());
            Assert.fail("Cannot open SMS page: " + e.getMessage());
        }
    }

    /**
     * Check if home page is loaded
     */
    public boolean isHomePageLoaded() {
        return isElementDisplayed(reservationsTab) || isElementDisplayed(testingTab);
    }
}

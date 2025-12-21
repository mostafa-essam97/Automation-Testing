package Pages;

import Utilities.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

/**
 * SimtestReserveFreeSlotPage - Page Object for free slot reservation in SIMTest
 */
public class simTestReserveFreeSlotPage extends BasePage {

    public simTestReserveFreeSlotPage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(id = "btnFindFreeSlots")
    private WebElement findSlotBtn;

    @FindBy(id = "dlgFreeSlots")
    private WebElement freeSlotsDialog;

    @FindBy(xpath = "//*[@id='divFreeSlots']/div[contains(@style,'display: inline-block')]")
    private List<WebElement> freeNumberSlots;

    @FindBy(xpath = "/html/body/div[13]/div")
    private WebElement confirmReservationPopup;

    @FindBy(xpath = "/html/body/div[13]/div/div/p")
    private WebElement confirmationMessage;

    // ============ Actions ============

    /**
     * Click the Find Free Slots button
     */
    public void clickFindFreeSlots() {
        String expectedBtnText = "FIND FREE SLOTS";
        try {
            logger.info("Looking for Find Free Slots button...");
            
            String btnText = findSlotBtn.getText();
            if (btnText.equalsIgnoreCase(expectedBtnText)) {
                click(findSlotBtn);
                logger.info("Find Free Slots button clicked");
            } else {
                String number = TestContext.getData().getFullReservedNumber();
                logger.error("Find Free Slots button not available for number: {}", number);
                Assert.fail("Find Free Slots button not available for: " + number);
            }
        } catch (Exception e) {
            logger.error("Failed to click Find Free Slots: {}", e.getMessage());
            Assert.fail("Cannot click Find Free Slots button: " + e.getMessage());
        }
    }

    /**
     * Choose a free slot by index
     */
    public void chooseFreeSlotByIndex(int index) {
        try {
            logger.info("Selecting free slot at index: {}", index);
            
            // Wait for dialog
            wait.until(ExpectedConditions.visibilityOf(freeSlotsDialog));

            if (freeNumberSlots.isEmpty()) {
                Assert.fail("No free slots available. Try again later.");
            }

            if (index < 0 || index >= freeNumberSlots.size()) {
                Assert.fail("Invalid index: " + index + ". Available slots: " + freeNumberSlots.size());
            }

            // Get and click the slot
            WebElement targetSlot = freeNumberSlots.get(index);
            WebElement reserveBtn = targetSlot.findElement(By.tagName("button"));
            click(reserveBtn);

            logger.info("Selected {} slot", getOrdinal(index + 1));

            // Wait for confirmation popup
            wait.until(ExpectedConditions.visibilityOf(confirmReservationPopup));
            
            // Verify success message
            String message = confirmationMessage.getText();
            Assert.assertEquals(message, "Reservation successfully created",
                    "Expected success message but got: " + message);

            logger.info("Number reserved successfully");

        } catch (Exception e) {
            logger.error("Failed to reserve slot at index {}: {}", index, e.getMessage());
            Assert.fail("Cannot reserve free slot at index " + index + ": " + e.getMessage());
        }
    }
}

package Pages;

import Utilities.testDataHolder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class simTestReserveFreeSlotPage extends pageBase{
    public simTestReserveFreeSlotPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    //Define Elements
    @FindBy(id = "btnFindFreeSlots")
    WebElement findSlot;

    @FindBy(id = "dlgFreeSlots")
    WebElement freeSlotsDialog;

    @FindBy(xpath = "//*[@id=" +
            "'divFreeSlots']/div[contains(@style,'display: inline-block')]")
    List<WebElement> freeNumberSlots;

    @FindBy(xpath = "/html/body/div[13]/div")
    WebElement confirmReservationPopup;

    @FindBy(xpath = "/html/body/div[13]/div/div/p")
    WebElement confirmationMessage;


    //Define Functions
    public void accessFreeSlotsBtn() {
        String expectedBtnText = "FIND FREE SLOTS";
        try {
            if (findSlot.getText().equalsIgnoreCase(expectedBtnText)) {
                findSlot.click();
                System.out.println("The ' find free slot ' button is available and clicked, Please select one free slot. ");
            } else {
                System.out.println("The ' find free slot ' button doesn't available for this number. " + testDataHolder.fullReservedNumberData);
                Assert.fail("The ' find free slots' button doesn't available for this number. " + testDataHolder.fullReservedNumberData);
            }
        } catch (Exception e) {
            System.out.println("Something want wrong, Please retry. " + e.getMessage());
        }
    }

    public void chooseFreeSlotByIndex(int index) {
        try {
            wait.until(ExpectedConditions.visibilityOf(freeSlotsDialog));
            if (freeNumberSlots.isEmpty()) {
                Assert.fail("There are no free slots current now. Retry later. ");
            }
            if (index < 0 || index >= freeNumberSlots.size()) {
                Assert.fail(index + " is invalid index. The available slots count is " + freeNumberSlots.size());
            }
            if (index >= 0 && index <= freeNumberSlots.size()) {
                WebElement targetslot = freeNumberSlots.get(index);
                WebElement reserveFreeSlotBtn = targetslot.findElement(By.tagName("button"));
                reserveFreeSlotBtn.click();
                System.out.println("You have been chose the " + getOrdinal(index + 1) + " slot.");
                wait.until(ExpectedConditions.visibilityOf(confirmReservationPopup));
                Assert.assertEquals(confirmationMessage.getText(), "Reservation successfully created");
                System.out.println("The number reserved successfully. ");
            }
        } catch (Exception e) {
            System.out.println("Something want wrong, Please retry. " + e.getMessage());
            Assert.fail("Can't reserve the free number slot at index = " + index + "\n" + e.getMessage());
        }
    }
}

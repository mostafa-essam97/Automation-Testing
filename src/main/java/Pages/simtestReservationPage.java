package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class simtestReservationPage extends pageBase {
    public String reservedNumberText = "";
    public String fullReservedNumber = "";
    public String reservedNumbercountryCode = "";

    public simtestReservationPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Define Elements

    @FindBy(css = "ul.modem-list-main li")
    List<WebElement> modemNumberList;

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

    //Define Setter&Getter Functions
    public String getFullReservedNumber() {
        return fullReservedNumber;
    }

    public String getReservedNumbercountryCode() {
        return reservedNumbercountryCode;
    }

    // Define Functions

    public void selectCountry(String countryName) {
        try {
            WebElement menu = driver.findElement(By.xpath("//span[contains(@class,'k-input') and text()='Select a country...']"));
            wait.until(ExpectedConditions.visibilityOf(menu));
            menu.click();
            Thread.sleep(2000);
            List<WebElement> countries = driver.findElements(By.cssSelector("li[role='option']"));
            boolean isCountryFound = false;
            for (WebElement country : countries) {
                if (country.getText().matches(countryName)) {
                    country.click();
                    System.out.println("You have select " + countryName + " from menu successfully.");
                    Thread.sleep(2000);
                    isCountryFound = true;
                    break;
                }
            }
            if (!isCountryFound){
                System.out.println("❌The Country '" + countryName + "' is not found in the list.");
                Assert.fail("Country '" + countryName + "' is not found in the list.");
            }

        } catch (Exception e) {
            System.out.println("❌ Country '" + countryName + "' not found in the list." + e.getMessage());
            Assert.fail("Can't find country: '" + countryName + "' The error message is: " + e.getMessage());
        }
    }

    public void chooseNumberByIndex(int index) {

        try {
            // Check index boundaries
            if (index < 0 || index >= modemNumberList.size()) {
                Assert.fail(index + " is invalid index. The available numbers count is: " + modemNumberList.size());
            }

            // Get number by index
            WebElement number = modemNumberList.get(index);

            // Wait for the number element to be visible
            wait.until(ExpectedConditions.visibilityOf(number));

            // Find the clickable link inside the number and click it
            WebElement link = number.findElement(By.tagName("a"));
            wait.until(ExpectedConditions.elementToBeClickable(link)).click();

            reservedNumberText = modemNumberList.get(index).getText().trim();
            System.out.println("Clicked on number " + " ' " + modemNumberList.get(index).getText() +
                    " and this number is the " + getOrdinal(index + 1) + " in the country numbers list.");

// نستخدم Regex عشان نجيب الرقم اللي بيبدأ بـ 00 ومكون من أرقام فقط
            Pattern pattern = Pattern.compile("00\\d+"); // يعني رقم بيبدأ بـ 00 وبعده أرقام
            Matcher matcher = pattern.matcher(reservedNumberText);

            if (matcher.find()) {
                String fullNumber = matcher.group(); // مثلًا: 0097332014805
                fullReservedNumber = fullNumber.substring(2); // نشيل أول صفرين => 97332014805
                System.out.println("Extracted number only: '" + fullReservedNumber + "'");
                reservedNumbercountryCode = fullReservedNumber.substring(0, 3);
                System.out.println("The country code is==> " + reservedNumbercountryCode);
            } else {
                System.out.println("❌ Couldn't find a valid number in the text.");
            }
        } catch (Exception e) {
            Assert.fail("Failed to click number at index " + getOrdinal(index + 1) + ": " + e.getMessage());
        }
    }

    public void accessFreeSlotsBtn() {
        String expectedBtnText = "FIND FREE SLOTS";
        try {
            WebElement button = driver.findElement(By.id("btnFindFreeSlots"));
            if (button.getText().equalsIgnoreCase(expectedBtnText)) {
                findSlot.click();
                System.out.println("The ' find free slot ' button is available and clicked, Please select one free slot. ");
            } else {
                System.out.println("The ' find free slot ' button doesn't available for this number. " + fullReservedNumber);
                Assert.fail("The ' find free slots' button doesn't available for this number. " + fullReservedNumber);
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

    public void verifyReservedNumberInActiveList(String expectedNumber) {
        System.out.println("🔍 Waiting for number to appear in ACTIVE list: '" + expectedNumber + "'");

        int maxRetries = 10; // نحاول 10 مرات كل 30 ثانية
        int retryCount = 0;
        boolean found = false;

        while (retryCount < maxRetries && !found) {
            driver.navigate().refresh();
            System.out.println("🔄 Refreshed the page | Attempt #" + (retryCount + 1));

            try {
                // ✅ استنى العناصر تتحدث بعد الريفريش
                wait.until(ExpectedConditions.presenceOfElementLocated(By.id("active_reservations")));

                // ننتظر ظهور قائمة ACTIVE
                List<WebElement> activeList = wait.until(
                        ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("#active_reservations li")));
                if (activeList.isEmpty()){
                    System.out.println("⏳ Empty active list, retrying in 30 seconds...");
                    Thread.sleep(20000);
                    System.out.println("🔄 Refreshed the page | Attempt #" + (retryCount + 1));
                    retryCount++;
                    continue;
                }

                for (WebElement item : activeList) {
                    String itemText = item.getText().trim();
                    System.out.println("🧪 Comparing item text: [" + itemText + "] with expected: [" + expectedNumber + "]");

                    if (expectedNumber.replaceAll("\\s+", "").contains(itemText.replaceAll("\\s+", ""))) {
                        System.out.println("✅ Found number in ACTIVE list: " + itemText);
                        System.out.println("The reserved number is ==> '" + fullReservedNumber + "'");
                        WebElement link = item.findElement(By.tagName("a"));
                        link.click(); // نضغط على الرقم
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    retryCount++;
                    System.out.println("⏳ Not found yet, retrying in 30 seconds...");

                    Thread.sleep(30000);
                }

            } catch (Exception e) {
                retryCount++;
                System.out.println("⚠️ Error during check Empty active list: " + e.getMessage() + " | Retrying...");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ignored) {
                }
            }
        }
        if (!found) {
            Assert.fail("❌ Reserved number '" + expectedNumber + "' not found in ACTIVE list after " + maxRetries + " attempts.");
        }
    }
}
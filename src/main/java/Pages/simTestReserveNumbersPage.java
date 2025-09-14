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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class simTestReserveNumbersPage extends pageBase{
    public simTestReserveNumbersPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Define Elements

    @FindBy(css = "ul.modem-list-main li")
    List<WebElement> modemNumberList;

    //Define Functions
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

            String reservedNumberText = modemNumberList.get(index).getText().trim();
            System.out.println("Clicked on number " + " ' " + modemNumberList.get(index).getText() +
                    " and this number is the " + getOrdinal(index + 1) + " in the country numbers list.");

// نستخدم Regex عشان نجيب الرقم اللي بيبدأ بـ 00 ومكون من أرقام فقط
            Pattern pattern = Pattern.compile("00\\d+"); // يعني رقم بيبدأ بـ 00 وبعده أرقام
            Matcher matcher = pattern.matcher(reservedNumberText);

            if (matcher.find()) {
                String fullNumber = matcher.group(); // مثلًا: 0097332014805
                String fullReservedNumber = fullNumber.substring(2); // نشيل أول صفرين => 97332014805
                String countryCodeOfReservedNumber = fullReservedNumber.substring(0, 3);

                testDataHolder.reservedNumberTextData = reservedNumberText;
                testDataHolder.countryCodeData = countryCodeOfReservedNumber;
                testDataHolder.fullReservedNumberData = fullReservedNumber;

                System.out.println("The country code is==> " + countryCodeOfReservedNumber);
                System.out.println("Extracted number is==> '" + fullReservedNumber + "'");


            } else {
                System.out.println("❌ Couldn't find a valid number in the text.");
            }
        } catch (Exception e) {
            Assert.fail("Failed to click number at index " + getOrdinal(index + 1) + ": " + e.getMessage());
        }
    }
}

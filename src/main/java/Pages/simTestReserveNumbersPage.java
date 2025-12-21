package Pages;

import Utilities.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SimtestReserveNumbersPage - Page Object for number reservation in SIMTest
 */
public class simTestReserveNumbersPage extends BasePage {

    // Pattern to match numbers starting with 00
    private static final Pattern NUMBER_PATTERN = Pattern.compile("00\\d+");

    public simTestReserveNumbersPage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(css = "ul.modem-list-main li")
    private List<WebElement> modemNumberList;

    // ============ Actions ============

    /**
     * Choose a number by index and extract its details
     */
    public void chooseNumberByIndex(int index) {
        try {
            logger.info("Selecting number at index: {}", index);
            
            // Wait for numbers to load
            longWait.until(ExpectedConditions.visibilityOfAllElements(modemNumberList));

            // Validate index
            if (index < 0 || index >= modemNumberList.size()) {
                Assert.fail("Invalid index: " + index + ". Available numbers: " + modemNumberList.size());
            }

            // Get the number element
            WebElement numberElement = modemNumberList.get(index);
            wait.until(ExpectedConditions.visibilityOf(numberElement));

            // Find and click the link
            WebElement link = numberElement.findElement(By.tagName("a"));
            waitForClickable(link).click();

            // Extract number text
            String reservedNumberText = numberElement.getText().trim();
            logger.info("Clicked on {} number: {}", getOrdinal(index + 1), reservedNumberText);

            // Extract and store number details
            extractAndStoreNumberDetails(reservedNumberText);

        } catch (Exception e) {
            logger.error("Failed to select number at index {}: {}", index, e.getMessage());
            Assert.fail("Failed to click number at index " + getOrdinal(index + 1) + ": " + e.getMessage());
        }
    }

    /**
     * Extract number details and store in TestContext
     */
    private void extractAndStoreNumberDetails(String reservedNumberText) {
        Matcher matcher = NUMBER_PATTERN.matcher(reservedNumberText);

        if (matcher.find()) {
            String fullNumber = matcher.group(); // e.g., 0097332014805
            String fullReservedNumber = fullNumber.substring(2); // Remove leading 00
            String countryCode = fullReservedNumber.substring(0, 3);

            // Store in thread-safe TestContext
            TestContext.TestData data = TestContext.getData();
            data.setReservedNumberText(reservedNumberText);
            data.setCountryCode(countryCode);
            data.setFullReservedNumber(fullReservedNumber);

            logger.info("Number extracted successfully:");
            logger.info("   Full text: {}", reservedNumberText);
            logger.info("   Country code: {}", countryCode);
            logger.info("   Number: {}", fullReservedNumber);
        } else {
            logger.error("Could not extract valid number from: {}", reservedNumberText);
            Assert.fail("Could not find valid number in text: " + reservedNumberText);
        }
    }

    /**
     * Get count of available numbers
     */
    public int getNumbersCount() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(modemNumberList));
            return modemNumberList.size();
        } catch (Exception e) {
            logger.warn("Could not count numbers: {}", e.getMessage());
            return 0;
        }
    }
}

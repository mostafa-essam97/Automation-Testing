package Pages;

import Utilities.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

/**
 * SimtestActiveNumbersPage - Page Object for active numbers list in SIMTest
 */
public class simTestActiveNumbersPage extends BasePage {

    private static final int MAX_RETRIES = 10;
    private static final int RETRY_WAIT_SECONDS = 30;

    public simTestActiveNumbersPage(WebDriver driver) {
        super(driver);
    }

    // ============ Actions ============

    /**
     * Verify that a reserved number appears in the active list
     * Retries multiple times with page refresh
     */
    public void verifyReservedNumberInActiveList(String expectedNumber) {
        logger.info("Waiting for number to appear in ACTIVE list: '{}'", expectedNumber);

        int retryCount = 0;
        boolean found = false;

        while (retryCount < MAX_RETRIES && !found) {
            driver.navigate().refresh();
            logger.info("Page refreshed | Attempt #{}", retryCount + 1);

            try {
                // Wait for the active reservations container
                longWait.until(ExpectedConditions.presenceOfElementLocated(By.id("active_reservations")));

                // Get active list items
                List<WebElement> activeList = wait.until(
                        ExpectedConditions.visibilityOfAllElementsLocatedBy(
                                By.cssSelector("#active_reservations li")));

                if (activeList.isEmpty()) {
                    logger.info("Empty active list, waiting {} seconds...", RETRY_WAIT_SECONDS);
                    waitSeconds(RETRY_WAIT_SECONDS);
                    retryCount++;
                    continue;
                }

                // Search for the expected number
                for (WebElement item : activeList) {
                    String itemText = item.getText().trim();
                    logger.debug("Comparing: [{}] with expected: [{}]", itemText, expectedNumber);

                    // Compare without whitespace
                    String normalizedItem = itemText.replaceAll("\\s+", "");
                    String normalizedExpected = expectedNumber.replaceAll("\\s+", "");

                    if (normalizedExpected.contains(normalizedItem) || normalizedItem.contains(normalizedExpected)) {
                        logger.info("Found number in ACTIVE list: {}", itemText);
                        
                        // Click on the number
                        WebElement link = item.findElement(By.tagName("a"));
                        link.click();
                        
                        String fullNumber = TestContext.getData().getFullReservedNumber();
                        logger.info("Reserved number: {}", fullNumber);
                        
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    logger.info("Number not found yet, waiting {} seconds...", RETRY_WAIT_SECONDS);
                    waitSeconds(RETRY_WAIT_SECONDS);
                    retryCount++;
                }

            } catch (Exception e) {
                logger.warn("Error during check: {} | Retrying...", e.getMessage());
                waitSeconds(RETRY_WAIT_SECONDS);
                retryCount++;
            }
        }

        if (!found) {
            Assert.fail("Reserved number '" + expectedNumber + "' not found in ACTIVE list after " 
                    + MAX_RETRIES + " attempts");
        }
    }

    /**
     * Wait for specified seconds
     */
    private void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }
}

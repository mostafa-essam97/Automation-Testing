package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

/**
 * SimtestSelectCountryPage - Page Object for country selection in SIMTest
 */
public class simtestSelectCountryPage extends BasePage {

    private static final int MAX_RETRY_ATTEMPTS = 3;

    public simtestSelectCountryPage(WebDriver driver) {
        super(driver);
    }

    // ============ Actions ============

    /**
     * Select a country from the dropdown by name
     */
    public void selectCountry(String countryName) {
        int attempt = 1;
        boolean isCountryFound = false;

        while (attempt <= MAX_RETRY_ATTEMPTS && !isCountryFound) {
            try {
                logger.info("Attempt #{} to select country: {}", attempt, countryName);

                // Find and click the country dropdown
                WebElement menu = driver.findElement(
                        By.xpath("//span[contains(@class,'k-input') and text()='Select a country...']"));
                wait.until(ExpectedConditions.visibilityOf(menu));
                menu.click();
                
                // Wait for dropdown to open
                waitForPageStability();

                // Find all country options
                List<WebElement> countries = driver.findElements(By.cssSelector("li[role='option']"));
                
                for (WebElement country : countries) {
                    if (country.getText().trim().equalsIgnoreCase(countryName.trim())) {
                        country.click();
                        logger.info("Selected country: {}", countryName);
                        waitForPageStability();
                        isCountryFound = true;
                        break;
                    }
                }

                if (!isCountryFound) {
                    logger.warn("Country '{}' not found in attempt #{}", countryName, attempt);
                    attempt++;
                }

            } catch (StaleElementReferenceException staleEx) {
                logger.warn("StaleElementException - retrying... Attempt #{}", attempt);
                attempt++;
            } catch (Exception e) {
                logger.error("Error during country selection: {}", e.getMessage());
                Assert.fail("Cannot find country: '" + countryName + "' | Error: " + e.getMessage());
            }
        }

        if (!isCountryFound) {
            Assert.fail("Failed to find country '" + countryName + "' after " + MAX_RETRY_ATTEMPTS + " attempts");
        }
    }
}

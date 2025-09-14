package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import Utilities.testDataHolder;


import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class simtestSelectCountryPage extends pageBase {
    public simtestSelectCountryPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Define Elements


    // Define Functions
    public void selectCountry(String countryName) {
        int maxAttempts = 3;
        int attempt = 1;
        boolean isCountryFound = false;

        while (attempt <= maxAttempts && !isCountryFound) {
            try {
                System.out.println("🔁 Attempt #" + attempt + " to select country: " + countryName);

                WebElement menu = driver.findElement(By.xpath("//span[contains(@class,'k-input') and text()='Select a country...']"));
                wait.until(ExpectedConditions.visibilityOf(menu));
                menu.click();
                Thread.sleep(2000);

                List<WebElement> countries = driver.findElements(By.cssSelector("li[role='option']"));
                for (WebElement country : countries) {
                    if (country.getText().trim().equalsIgnoreCase(countryName.trim())) {
                        country.click();
                        System.out.println("✅ You have selected '" + countryName + "' successfully.");
                        Thread.sleep(2000);
                        isCountryFound = true;
                        break;
                    }
                }

                if (!isCountryFound) {
                    System.out.println("❌ The Country '" + countryName + "' is not found in the list.");
                    attempt++;
                    Thread.sleep(1000); // Pause before retry
                }

            } catch (org.openqa.selenium.StaleElementReferenceException staleEx) {
                System.out.println("⚠️ StaleElementException detected. Retrying... Attempt #" + attempt);
                attempt++;
            } catch (Exception e) {
                System.out.println("❌ General error during country selection: " + e.getMessage());
                Assert.fail("Can't find country: '" + countryName + "' | Error: " + e.getMessage());
            }
        }

        if (!isCountryFound) {
            Assert.fail("❌ Failed to find country '" + countryName + "' after " + maxAttempts + " attempts.");
        }
    }
}
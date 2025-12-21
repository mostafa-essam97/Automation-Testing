package Pages;

import Utilities.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ShofhaPackagesPage - Page Object for Shofha subscription packages
 */
public class ShofhaPackagesPage extends BasePage {

    public ShofhaPackagesPage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(css = "div.PKG .col-md-4")
    private List<WebElement> packages;

    @FindBy(id = "errortext1")
    private WebElement packagesErrorMsg;

    // ============ Actions ============

    /**
     * Select a package by index (0-based)
     */
    public void selectPackage(int index) {
        try {
            logger.info("Selecting package at index: {}", index);
            
            // Wait for packages to be visible
            wait.until(ExpectedConditions.visibilityOfAllElements(packages));

            if (packages.isEmpty()) {
                logger.error("❌ No packages found on the page");
                Assert.fail("No packages available to select");
                return;
            }

            if (index < 0 || index >= packages.size()) {
                logger.error("❌ Invalid index: {}. Available packages: {}", index, packages.size());
                Assert.fail("Invalid package index: " + index + ". Available: " + packages.size());
                return;
            }

            WebElement selectedPackage = packages.get(index);

            // Extract package info
            String packageType = selectedPackage.findElement(By.tagName("span")).getText().trim();
            String packagePrice = selectedPackage.findElement(By.className("CustomPrice")).getText().trim();

            // Record subscription timestamp
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String subscriptionTimestamp = LocalDateTime.now().format(formatter);

            // Store in TestContext (thread-safe)
            TestContext.TestData data = TestContext.getData();
            data.setSubscriptionTimestamp(subscriptionTimestamp);
            data.setPackageType(packageType);
            data.setPackagePrice(packagePrice);

            logger.info("👉 Selecting {} package - {}", getOrdinal(index + 1), packageType);

            // Click subscribe button
            WebElement subscribeBtn = selectedPackage.findElement(By.tagName("button"));
            waitForClickable(subscribeBtn);
            
            // Small wait for any animations
            waitForPageStability();
            
            subscribeBtn.click();

            // Check for error messages
            if (isElementDisplayed(packagesErrorMsg)) {
                String errorText = packagesErrorMsg.getText();
                logger.error("❌ Error after selecting package: {}", errorText);
                Assert.fail("Subscription failed. Error: " + errorText);
                return;
            }

            logger.info("✅ Package selected successfully:");
            logger.info("   Type: {}", packageType);
            logger.info("   Price: {}", packagePrice);
            logger.info("   Time: {}", subscriptionTimestamp);

        } catch (Exception e) {
            logger.error("❌ Failed to select package: {}", e.getMessage());
            Assert.fail("Exception while selecting package: " + e.getMessage());
        }
    }

    /**
     * Get the number of available packages
     */
    public int getPackageCount() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(packages));
            return packages.size();
        } catch (Exception e) {
            logger.warn("Could not count packages: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Select package by type name (Daily, Weekly, Monthly)
     */
    public void selectPackageByType(String typeName) {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(packages));

            for (int i = 0; i < packages.size(); i++) {
                WebElement pkg = packages.get(i);
                String pkgType = pkg.findElement(By.tagName("span")).getText().trim();
                
                if (pkgType.toLowerCase().contains(typeName.toLowerCase())) {
                    logger.info("Found package type '{}' at index {}", typeName, i);
                    selectPackage(i);
                    return;
                }
            }

            logger.error("Package type '{}' not found", typeName);
            Assert.fail("Package type not found: " + typeName);
        } catch (Exception e) {
            logger.error("Failed to select package by type: {}", e.getMessage());
            Assert.fail("Failed to select package by type: " + e.getMessage());
        }
    }

    /**
     * Check if packages are displayed
     */
    public boolean arePackagesDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(packages));
            return !packages.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}






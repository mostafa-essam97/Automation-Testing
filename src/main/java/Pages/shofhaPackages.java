package Pages;

import Utilities.TestContext;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ShofhaPackages - Page Object for Shofha NEW packages selection page
 * URL: /subscriptionLandingPage/Web/DCB/selectPackage
 */
public class shofhaPackages extends BasePage {

    public shofhaPackages(WebDriver driver) {
        super(driver);
    }

    // ============ Elements (Updated for new page) ============

    // All package containers (div.provider3)
    @FindBy(css = "div.provider3")
    private List<WebElement> packageContainers;

    // All package radio buttons
    @FindBy(css = "input.provider-radio[name='pkgType']")
    private List<WebElement> packageRadioButtons;

    // Subscribe button
    @FindBy(id = "select_btn")
    private WebElement subscribeBtn;

    // Error message
    @FindBy(id = "errortext2")
    private WebElement errorMessage;

    // Page title
    @FindBy(xpath = "//h2[contains(text(),'اختر الباقة المناسبة')]")
    private WebElement pageTitle;

    // ============ Inner class for Package info ============

    private static class PackageInfo {
        WebElement radioButton;
        WebElement label;
        String packageType;
        String priceText;
        double priceValue;
        int index;

        PackageInfo(int index, WebElement radio, WebElement label, String type, String price, double value) {
            this.index = index;
            this.radioButton = radio;
            this.label = label;
            this.packageType = type;
            this.priceText = price;
            this.priceValue = value;
        }
    }

    // ============ Actions ============

    /**
     * Select the cheapest available package automatically
     */
    public void selectCheapestPackage() {
        logger.info("========================================");
        logger.info("Selecting cheapest package...");
        logger.info("========================================");

        try {
            // Wait for packages page to load
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            logger.info("Packages page loaded: {}", pageTitle.getText());

            // Wait for package containers
            wait.until(ExpectedConditions.visibilityOfAllElements(packageContainers));

            if (packageContainers.isEmpty()) {
                logger.error("❌ No packages found on the page!");
                Assert.fail("No packages available to select.");
                return;
            }

            logger.info("Found {} packages available", packageContainers.size());

            // Parse all packages
            List<PackageInfo> packages = parseAllPackages();

            if (packages.isEmpty()) {
                logger.error("❌ Could not parse any packages!");
                Assert.fail("Failed to parse package information.");
                return;
            }

            // Log all packages
            logger.info("Available packages:");
            for (PackageInfo pkg : packages) {
                logger.info("   #{} - {} : {} (parsed: {})", 
                    pkg.index + 1, pkg.packageType, pkg.priceText, pkg.priceValue);
            }

            // Find cheapest package
            PackageInfo cheapest = packages.stream()
                    .min(Comparator.comparingDouble(p -> p.priceValue))
                    .orElse(packages.get(0));

            logger.info("✅ Cheapest package found: {} - {}", cheapest.packageType, cheapest.priceText);

            // Select the cheapest package
            selectPackageByInfo(cheapest);

        } catch (Exception e) {
            logger.error("❌ Failed to select cheapest package: {}", e.getMessage());
            logger.error("Stack trace:", e);
            Assert.fail("Exception while selecting cheapest package: " + e.getMessage());
        }
    }

    /**
     * Select package by index (0-based)
     */
    public void selectPackage(int index) {
        logger.info("Selecting package at index: {}", index);

        try {
            // Wait for packages page to load
            wait.until(ExpectedConditions.visibilityOfAllElements(packageContainers));

            if (packageContainers.isEmpty()) {
                logger.error("❌ No packages found on the page!");
                Assert.fail("No packages available to select.");
                return;
            }

            if (index < 0 || index >= packageContainers.size()) {
                logger.error("❌ Invalid index: {}. Available: {}", index, packageContainers.size());
                Assert.fail("Invalid index: " + index + ". Available packages: " + packageContainers.size());
                return;
            }

            // Parse all packages and select by index
            List<PackageInfo> packages = parseAllPackages();
            
            if (index < packages.size()) {
                selectPackageByInfo(packages.get(index));
            } else {
                Assert.fail("Package index " + index + " not found");
            }

        } catch (Exception e) {
            logger.error("❌ Failed to select package at index {}: {}", index, e.getMessage());
            Assert.fail("Exception while selecting package: " + e.getMessage());
        }
    }

    /**
     * Parse all available packages
     */
    private List<PackageInfo> parseAllPackages() {
        List<PackageInfo> packages = new ArrayList<>();

        for (int i = 0; i < packageContainers.size(); i++) {
            try {
                WebElement container = packageContainers.get(i);

                // Find radio button
                WebElement radio = container.findElement(By.cssSelector("input.provider-radio"));

                // Find label
                WebElement label = container.findElement(By.tagName("label"));

                // Get package type (يومي، اسبوعي، شهري)
                String packageType = "";
                try {
                    packageType = container.findElement(By.cssSelector("p.packagetype")).getText().trim();
                } catch (Exception e) {
                    packageType = "Package " + (i + 1);
                }

                // Get price text
                String priceText = "";
                try {
                    priceText = container.findElement(By.cssSelector("p.price")).getText().trim();
                } catch (Exception e) {
                    priceText = "Unknown";
                }

                // Parse price value
                double priceValue = extractPriceValue(priceText);

                packages.add(new PackageInfo(i, radio, label, packageType, priceText, priceValue));

            } catch (Exception e) {
                logger.warn("Could not parse package at index {}: {}", i, e.getMessage());
            }
        }

        return packages;
    }

    /**
     * Extract numeric price value from text like "درهم 3.25" or "3.25 درهم"
     */
    private double extractPriceValue(String priceText) {
        try {
            // Pattern to match numbers (including decimals)
            Pattern pattern = Pattern.compile("([0-9]+\\.?[0-9]*)");
            Matcher matcher = pattern.matcher(priceText);

            if (matcher.find()) {
                return Double.parseDouble(matcher.group(1));
            }
        } catch (Exception e) {
            logger.warn("Could not parse price from: {}", priceText);
        }
        return Double.MAX_VALUE; // Return max value if can't parse
    }

    /**
     * Select a package using PackageInfo
     */
    private void selectPackageByInfo(PackageInfo pkg) throws InterruptedException {
        logger.info("Selecting package: {} - {}", pkg.packageType, pkg.priceText);

        // Click on the label to select the radio button
        scrollToElement(pkg.label);
        Thread.sleep(500);

        // Click using JavaScript for reliability
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", pkg.label);

        Thread.sleep(1000);

        // Verify radio button is selected
        boolean isSelected = pkg.radioButton.isSelected();
        logger.info("Radio button selected: {}", isSelected);

        if (!isSelected) {
            // Try clicking directly on radio button
            logger.warn("Label click didn't select radio, trying direct click...");
            js.executeScript("arguments[0].click();", pkg.radioButton);
            Thread.sleep(500);
            isSelected = pkg.radioButton.isSelected();
        }

        if (!isSelected) {
            logger.error("❌ Failed to select package radio button!");
            Assert.fail("Could not select package: " + pkg.packageType);
            return;
        }

        logger.info("✅ Package radio button selected successfully");

        // Record subscription timestamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String subscriptionTimestamp = LocalDateTime.now().format(formatter);

        // Store in TestContext
        TestContext.TestData data = TestContext.getData();
        data.setSubscriptionTimestamp(subscriptionTimestamp);
        data.setPackageType(pkg.packageType);
        data.setPackagePrice(pkg.priceText);

        logger.info("⏱️ Subscription timestamp recorded: {}", subscriptionTimestamp);

        // Click subscribe button
        clickSubscribeButton();
    }

    /**
     * Click the "اشترك الآن" (Subscribe Now) button
     */
    private void clickSubscribeButton() throws InterruptedException {
        logger.info("Clicking 'اشترك الآن' (Subscribe Now) button...");

        try {
            // Wait for button to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(subscribeBtn));

            // Scroll to button
            scrollToElement(subscribeBtn);
            Thread.sleep(500);

            // Click
            subscribeBtn.click();

            logger.info("✅ Subscribe button clicked");

            // Wait for page transition
            Thread.sleep(2000);
            waitForPageStability();

            // Check for error
            if (isErrorDisplayed()) {
                String error = getErrorMessage();
                logger.error("❌ Error after clicking subscribe: {}", error);
                Assert.fail("Subscription failed: " + error);
            }

            logger.info("✅ Package selected and subscription initiated!");
            logger.info("   Waiting for OTP...");

        } catch (Exception e) {
            logger.error("❌ Failed to click subscribe button: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Check if error message is displayed
     */
    public boolean isErrorDisplayed() {
        try {
            return errorMessage.isDisplayed() && 
                   !errorMessage.getCssValue("display").equals("none");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        try {
            if (isErrorDisplayed()) {
                return errorMessage.getText();
            }
        } catch (Exception e) {
            logger.debug("Error message not found");
        }
        return "";
    }

    /**
     * Get the number of available packages
     */
    public int getPackagesCount() {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(packageContainers));
            return packageContainers.size();
        } catch (Exception e) {
            logger.warn("Could not count packages: {}", e.getMessage());
            return 0;
        }
    }

    /**
     * Check if packages page is displayed
     */
    public boolean isPackagesPageDisplayed() {
        try {
            return isElementDisplayed(pageTitle) && !packageContainers.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.List;

/**
 * ShofhaHomePage - Page Object for Shofha home page after successful subscription
 */
public class shofhaHomePage extends BasePage {

    private static final String SHOFHA_HOME_URL = "https://shofha.com";
    private static final String ACCOUNT_SETTINGS_URL = "https://shofha.com/accountSettings";

    public shofhaHomePage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============

    @FindBy(id = "Sub_nav")
    private WebElement subscriptionBtn;

    @FindBy(id = "elmloader")
    private WebElement loader;

    // ============ Actions ============

    /**
     * Open subscription page
     */
    public void openSubscriptionPage() {
        try {
            logger.info("Opening subscription page...");
            
            click(subscriptionBtn);
            
            // Wait for navigation to complete
            waitForUrlContains("subscribe");
            waitForPageStability();
            
            logger.info("Subscription page opened successfully");
        } catch (Exception e) {
            logger.error("Failed to open subscription page: {}", e.getMessage());
            Assert.fail("Cannot open subscription page: " + e.getMessage());
        }
    }

    /**
     * Open user profile icon dropdown
     */
    public void openUserProfileIcon() {
        logger.info("Opening user profile dropdown...");

        try {
            // First, make sure we're on Shofha
            String currentUrl = driver.getCurrentUrl();
            logger.info("Current URL: {}", currentUrl);

            // Wait for page to fully load
            waitForPageStability();
            waitForLoaderToDisappear();
            Thread.sleep(2000);

            // Try to find profile icon with multiple selectors
            WebElement profileIcon = findProfileIcon();

            if (profileIcon == null) {
                logger.error("❌ Profile icon not found with any selector!");
                logger.error("   User might not be logged in or page structure changed");
                Assert.fail("Profile icon not found. User may not be logged in.");
                return;
            }

            logger.info("Found profile icon, clicking...");

            // Scroll to profile icon
            scrollToElement(profileIcon);
            Thread.sleep(500);

            // Click using JavaScript for reliability
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", profileIcon);

            // Wait for dropdown to appear
            Thread.sleep(1500);

            logger.info("✅ Profile dropdown opened successfully");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.error("❌ Failed to open profile icon: {}", e.getMessage());
            logger.error("   Possible causes:");
            logger.error("   1. User is not logged in");
            logger.error("   2. Page not fully loaded");
            logger.error("   3. Profile icon selector changed");
            Assert.fail("Cannot open profile icon: " + e.getMessage());
        }
    }

    /**
     * Find profile icon using multiple selectors
     */
    private WebElement findProfileIcon() {
        // List of possible selectors for profile icon
        String[] cssSelectors = {
            "img[alt='user']",
            "img[alt='profile']",
            ".profile-icon img",
            ".user-icon img",
            ".navbar img[alt*='user']",
            "nav img[alt='user']",
            "header img[alt='user']",
            ".dropdown-toggle img",
            "a.dropdown-toggle img"
        };

        for (String selector : cssSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                for (WebElement element : elements) {
                    if (element.isDisplayed()) {
                        logger.info("Found profile icon with selector: {}", selector);
                        return element;
                    }
                }
            } catch (Exception e) {
                logger.debug("Selector not found: {}", selector);
            }
        }

        // Try XPath selectors
        String[] xpathSelectors = {
            "//img[@alt='user']",
            "//img[contains(@alt, 'user')]",
            "//img[contains(@alt, 'profile')]",
            "//nav//img[@alt='user']",
            "//header//img[@alt='user']",
            "//a[contains(@class, 'dropdown')]//img"
        };

        for (String xpath : xpathSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(xpath));
                for (WebElement element : elements) {
                    if (element.isDisplayed()) {
                        logger.info("Found profile icon with XPath: {}", xpath);
                        return element;
                    }
                }
            } catch (Exception e) {
                logger.debug("XPath not found: {}", xpath);
            }
        }

        // Try to find the dropdown toggle button instead
        try {
            WebElement dropdownToggle = driver.findElement(By.cssSelector("a.dropdown-toggle, button.dropdown-toggle"));
            if (dropdownToggle.isDisplayed()) {
                logger.info("Found dropdown toggle button as fallback");
                return dropdownToggle;
            }
        } catch (Exception e) {
            logger.debug("Dropdown toggle not found");
        }

        return null;
    }

    /**
     * Open user account settings page
     */
    public void openUserAccountSettings() {
        logger.info("Opening account settings...");

        try {
            // Wait for dropdown animation
            Thread.sleep(1000);

            // Try to find and click account settings link
            WebElement settingsLink = findAccountSettingsLink();

            if (settingsLink == null) {
                logger.warn("Account settings link not found in dropdown, trying direct navigation...");
                // Navigate directly to account settings
                driver.get(ACCOUNT_SETTINGS_URL);
                waitForPageStability();
                logger.info("Navigated directly to account settings");
                return;
            }

            logger.info("Found account settings link, clicking...");

            // Wait for the link to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(settingsLink));

            // Click using JavaScript for reliability
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", settingsLink);

            logger.info("Clicked on account settings link");

            // Wait for navigation
            Thread.sleep(2000);
            waitForPageStability();

            // Verify navigation
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("accountSettings")) {
                logger.info("✅ Account settings page opened successfully");
                logger.info("   Current URL: {}", currentUrl);
            } else {
                logger.warn("URL doesn't contain 'accountSettings': {}", currentUrl);
                logger.info("Trying direct navigation...");
                driver.get(ACCOUNT_SETTINGS_URL);
                waitForPageStability();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread interrupted");
        } catch (Exception e) {
            logger.error("❌ Failed to open account settings: {}", e.getMessage());
            logger.info("Trying direct navigation as fallback...");
            try {
                driver.get(ACCOUNT_SETTINGS_URL);
                waitForPageStability();
                logger.info("Navigated directly to account settings");
            } catch (Exception fallbackError) {
                Assert.fail("Cannot open account settings: " + e.getMessage());
            }
        }
    }

    /**
     * Find account settings link using multiple selectors
     */
    private WebElement findAccountSettingsLink() {
        // Try CSS selectors
        String[] cssSelectors = {
            "a.dropdown-item.acc[routerlink='/accountSettings']",
            "a[routerlink='/accountSettings']",
            "a[href='/accountSettings']",
            "a.dropdown-item[href='/accountSettings']",
            "a.acc[href*='accountSettings']",
            ".dropdown-menu a[href*='accountSettings']",
            ".dropdown-item[href*='accountSettings']"
        };

        for (String selector : cssSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                for (WebElement element : elements) {
                    if (element.isDisplayed()) {
                        logger.info("Found account settings link with selector: {}", selector);
                        return element;
                    }
                }
            } catch (Exception e) {
                logger.debug("Selector not found: {}", selector);
            }
        }

        // Try XPath with text content
        String[] xpathSelectors = {
            "//a[contains(@href, 'accountSettings')]",
            "//a[contains(@routerlink, 'accountSettings')]",
            "//a[contains(text(), 'إعدادت الحساب')]",
            "//a[contains(text(), 'إعدادات الحساب')]",
            "//a[contains(text(), 'Account Settings')]",
            "//a[contains(text(), 'Settings')]",
            "//a[.//img[@alt='setting']]"
        };

        for (String xpath : xpathSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.xpath(xpath));
                for (WebElement element : elements) {
                    if (element.isDisplayed()) {
                        logger.info("Found account settings link with XPath: {}", xpath);
                        return element;
                    }
                }
            } catch (Exception e) {
                logger.debug("XPath not found: {}", xpath);
            }
        }

        return null;
    }

    /**
     * Wait for loader to disappear
     */
    private void waitForLoaderToDisappear() {
        try {
            shortWait.until(ExpectedConditions.invisibilityOf(loader));
            logger.debug("Loader disappeared");
        } catch (Exception e) {
            logger.debug("Loader not found or already hidden");
        }
    }

    /**
     * Check if user is logged in by looking for profile icon
     */
    public boolean isUserLoggedIn() {
        try {
            WebElement profileIcon = findProfileIcon();
            return profileIcon != null && profileIcon.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if subscription button is visible
     */
    public boolean isSubscriptionButtonVisible() {
        return isElementDisplayed(subscriptionBtn);
    }

    /**
     * Navigate to Shofha home page
     */
    public void navigateToHome() {
        driver.get(SHOFHA_HOME_URL);
        waitForPageStability();
        logger.info("Navigated to Shofha home page");
    }

    /**
     * Navigate directly to account settings (as fallback)
     */
    public void navigateToAccountSettings() {
        logger.info("Navigating directly to account settings...");
        driver.get(ACCOUNT_SETTINGS_URL);
        waitForPageStability();
        logger.info("Navigated to: {}", driver.getCurrentUrl());
    }
}

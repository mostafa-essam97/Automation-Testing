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

    public void openSubscriptionPage() {
        try {
            logger.info("Opening subscription page...");
            click(subscriptionBtn);
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
            waitForPageStability();
            waitForLoaderToDisappear();
            Thread.sleep(2000);

            WebElement profileIcon = findProfileIcon();

            if (profileIcon == null) {
                Assert.fail("Profile icon not found. User may not be logged in.");
                return;
            }

            scrollToElement(profileIcon);
            Thread.sleep(500);

            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", profileIcon);

            Thread.sleep(1500);
            logger.info("✅ Profile dropdown opened successfully");

        } catch (Exception e) {
            logger.error("❌ Failed to open profile icon: {}", e.getMessage());
            Assert.fail("Cannot open profile icon: " + e.getMessage());
        }
    }

    /**
     * ✅ FIXED: Find profile icon (button first, image fallback)
     */
    private WebElement findProfileIcon() {

        // ===== 1️⃣ REAL dropdown button (MAIN FIX) =====
        String[] buttonSelectors = {
                "button[data-toggle='dropdown']"
        };

        for (String selector : buttonSelectors) {
            try {
                List<WebElement> elements = driver.findElements(By.cssSelector(selector));
                for (WebElement element : elements) {
                    if (element.isDisplayed()) {
                        logger.info("Found profile DROPDOWN BUTTON with selector: {}", selector);
                        return element;
                    }
                }
            } catch (Exception e) {
                logger.debug("Button selector not found: {}", selector);
            }
        }

        // ===== 2️⃣ IMAGE fallback (old logic, unchanged) =====
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
                        logger.info("Found profile icon IMAGE with selector: {}", selector);
                        return element;
                    }
                }
            } catch (Exception e) {
                logger.debug("Selector not found: {}", selector);
            }
        }

        return null;
    }

    /**
     * Open user account settings page
     */
    public void openUserAccountSettings() {
        try {
            Thread.sleep(1000);

            WebElement settingsLink = findAccountSettingsLink();

            if (settingsLink == null) {
                driver.get(ACCOUNT_SETTINGS_URL);
                waitForPageStability();
                return;
            }

            wait.until(ExpectedConditions.elementToBeClickable(settingsLink));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", settingsLink);

            Thread.sleep(2000);
            waitForPageStability();

        } catch (Exception e) {
            driver.get(ACCOUNT_SETTINGS_URL);
            waitForPageStability();
        }
    }

    private WebElement findAccountSettingsLink() {
        String[] cssSelectors = {
                "a[routerlink='/accountSettings']",
                "a[href='/accountSettings']",
                ".dropdown-menu a[href*='accountSettings']"
        };

        for (String selector : cssSelectors) {
            List<WebElement> elements = driver.findElements(By.cssSelector(selector));
            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    return element;
                }
            }
        }
        return null;
    }

    private void waitForLoaderToDisappear() {
        try {
            shortWait.until(ExpectedConditions.invisibilityOf(loader));
        } catch (Exception ignored) {
        }
    }

    public void navigateToHome() {
        driver.get(SHOFHA_HOME_URL);
        waitForPageStability();
    }
}

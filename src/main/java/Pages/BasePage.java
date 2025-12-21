package Pages;

import config.ConfigReader;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * BasePage - Base class for all Page Objects
 * Contains common methods and WebDriver/Wait initialization
 */
public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final WebDriverWait shortWait;
    protected final WebDriverWait longWait;
    protected final Logger logger;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.logger = LoggerFactory.getLogger(this.getClass());
        
        // Initialize waits from config
        int defaultTimeout = ConfigReader.getDefaultTimeout();
        int shortTimeout = ConfigReader.getShortTimeout();
        int longTimeout = ConfigReader.getLongTimeout();
        
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(defaultTimeout));
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(shortTimeout));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(longTimeout));
        
        PageFactory.initElements(driver, this);
    }

    /**
     * Click on element with wait
     */
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        logger.debug("Clicked on element: {}", getElementDescription(element));
    }

    /**
     * Send keys to element with wait
     */
    protected void sendKeys(WebElement element, String text) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).sendKeys(text);
        logger.debug("Entered text in element: {}", getElementDescription(element));
    }

    /**
     * Clear and send keys
     */
    protected void clearAndSendKeys(WebElement element, String text) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(element));
        el.clear();
        el.sendKeys(text);
        logger.debug("Cleared and entered text in element: {}", getElementDescription(element));
    }

    /**
     * Get text from element
     */
    protected String getText(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    /**
     * Check if element is displayed (with short wait)
     */
    protected boolean isElementDisplayed(WebElement element) {
        try {
            shortWait.until(ExpectedConditions.visibilityOf(element));
            return element.isDisplayed();
        } catch (Exception e) {
            logger.debug("Element not displayed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Wait for element to be visible
     */
    protected WebElement waitForVisibility(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Scroll to element using JavaScript
     */
    protected void scrollToElement(WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
            // Wait for scroll to complete
            waitForPageStability();
            logger.debug("Scrolled to element: {}", getElementDescription(element));
        } catch (Exception e) {
            logger.warn("Failed to scroll to element: {}", e.getMessage());
        }
    }

    /**
     * Wait for page to be stable (no loading)
     */
    protected void waitForPageStability() {
        try {
            wait.until(driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return js.executeScript("return document.readyState").equals("complete");
            });
        } catch (Exception e) {
            logger.debug("Page stability check failed: {}", e.getMessage());
        }
    }

    /**
     * Wait for URL to contain specific text
     */
    protected void waitForUrlContains(String urlPart) {
        wait.until(ExpectedConditions.urlContains(urlPart));
        logger.debug("URL now contains: {}", urlPart);
    }

    /**
     * Wait for URL to match exactly
     */
    protected void waitForUrl(String expectedUrl) {
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
        logger.debug("URL matched: {}", expectedUrl);
    }

    /**
     * Check current URL
     */
    public boolean checkCurrentURL(String expectedURL) {
        String currentURL = driver.getCurrentUrl();
        boolean match = currentURL.equals(expectedURL);
        if (match) {
            logger.info("URL matched - Expected: {}, Current: {}", expectedURL, currentURL);
        } else {
            logger.warn("URL mismatch - Expected: {}, Current: {}", expectedURL, currentURL);
        }
        return match;
    }

    /**
     * Get ordinal suffix for a number (1st, 2nd, 3rd, etc.)
     */
    public static String getOrdinal(int number) {
        if (number <= 0) return String.valueOf(number);

        if (number % 100 >= 11 && number % 100 <= 13) {
            return number + "th";
        }

        switch (number % 10) {
            case 1: return number + "st";
            case 2: return number + "nd";
            case 3: return number + "rd";
            default: return number + "th";
        }
    }

    /**
     * Execute JavaScript
     */
    protected Object executeScript(String script, Object... args) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript(script, args);
    }

    /**
     * Get element description for logging
     */
    private String getElementDescription(WebElement element) {
        try {
            String tag = element.getTagName();
            String id = element.getAttribute("id");
            String text = element.getText();
            if (id != null && !id.isEmpty()) {
                return tag + "#" + id;
            } else if (text != null && !text.isEmpty() && text.length() < 30) {
                return tag + "[" + text + "]";
            }
            return tag;
        } catch (Exception e) {
            return "element";
        }
    }
}






package TestUtilities;

import config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * DriverManager - Centralized WebDriver management
 * Supports Chrome, Firefox, and Edge browsers
 */
public class DriverManager {
    private static final Logger logger = LoggerFactory.getLogger(DriverManager.class);
    private static WebDriver driver;

    /**
     * Get or create WebDriver instance
     */
    public static WebDriver getDriver(String browserName) {
        if (driver == null) {
            driver = createDriver(browserName);
            configureDriver(driver);
        }
        return driver;
    }

    /**
     * Get existing driver (for use in listeners)
     */
    public static WebDriver getDriver() {
        return driver;
    }

    /**
     * Create new WebDriver based on browser name
     */
    private static WebDriver createDriver(String browserName) {
        boolean headless = ConfigReader.isHeadless();
        String browser = browserName != null ? browserName.toLowerCase() : "chrome";
        
        logger.info("Creating {} driver (headless: {})", browser, headless);

        switch (browser) {
            case "chrome":
                return createChromeDriver(headless);
            case "firefox":
                return createFirefoxDriver(headless);
            case "edge":
                return createEdgeDriver(headless);
            default:
                logger.warn("Unknown browser: {}. Defaulting to Chrome.", browserName);
                return createChromeDriver(headless);
        }
    }

    /**
     * Create Chrome WebDriver with options
     */
    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        
        // Common options
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");
        
        // Performance options
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        // Stability options - prevent timeout issues
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-background-networking");
        options.addArguments("--disable-sync");
        options.addArguments("--disable-translate");
        options.addArguments("--disable-features=TranslateUI");
        options.addArguments("--disable-default-apps");
        options.addArguments("--disable-hang-monitor");
        options.addArguments("--disable-prompt-on-repost");
        options.addArguments("--disable-client-side-phishing-detection");
        options.addArguments("--disable-component-update");
        options.addArguments("--disable-ipc-flooding-protection");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--enable-features=NetworkServiceInProcess2");
        options.addArguments("--force-color-profile=srgb");
        
        // Page load strategy - don't wait for all resources
        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.EAGER);
        
        // Ignore certificate errors
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--ignore-ssl-errors");
        
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        
        return new ChromeDriver(options);
    }

    /**
     * Create Firefox WebDriver with options
     */
    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        
        if (headless) {
            options.addArguments("--headless");
            options.addArguments("--width=1920");
            options.addArguments("--height=1080");
        }
        
        return new FirefoxDriver(options);
    }

    /**
     * Create Edge WebDriver with options
     */
    private static WebDriver createEdgeDriver(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        
        return new EdgeDriver(options);
    }

    /**
     * Configure driver timeouts and settings
     */
    private static void configureDriver(WebDriver driver) {
        int pageLoadTimeout = ConfigReader.getInt("page.load.timeout", 120);
        int scriptTimeout = ConfigReader.getInt("long.timeout", 45);
        
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(scriptTimeout));
        
        // Note: We're NOT setting implicit wait - using explicit waits only
        logger.info("Driver configured:");
        logger.info("   Page load timeout: {}s", pageLoadTimeout);
        logger.info("   Script timeout: {}s", scriptTimeout);
    }

    /**
     * Quit and cleanup driver
     */
    public static void quitDriver() {
        if (driver != null) {
            logger.info("Quitting WebDriver");
            try {
                driver.quit();
            } catch (Exception e) {
                logger.warn("Error quitting driver: {}", e.getMessage());
            } finally {
                driver = null;
            }
        }
    }
}

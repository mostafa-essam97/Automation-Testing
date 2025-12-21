package TestCases;

import TestUtilities.DriverManager;
import Utilities.TestContext;
import config.ConfigReader;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;

import java.time.Duration;

/**
 * TestBase - Base class for all test classes
 * Handles browser setup/teardown and common test configuration
 */
public class testBase {
    private static final Logger logger = LoggerFactory.getLogger(testBase.class);
    protected static WebDriver driver;
    
    private static final int MAX_NAVIGATION_RETRIES = 3;
    private static final int WAIT_BETWEEN_RETRIES = 3000; // 3 seconds

    @BeforeSuite(alwaysRun = true)
    @Parameters({"browser"})
    public void setupSuite(@Optional("") String browserParam) {
        // Use parameter if provided, otherwise use config
        String browserName = (browserParam != null && !browserParam.isEmpty()) 
                ? browserParam 
                : ConfigReader.getBrowser();
        
        logger.info("========================================");
        logger.info("Starting Test Suite");
        logger.info("Browser: {}", browserName);
        logger.info("Page Load Timeout: {}s", ConfigReader.getInt("page.load.timeout", 120));
        logger.info("========================================");

        if (driver == null) {
            driver = DriverManager.getDriver(browserName);
            
            String loginUrl = ConfigReader.getSimtestLoginUrl();
            navigateWithRetry(loginUrl);
        }
    }

    /**
     * Navigate to URL with retry logic for timeout issues
     */
    private void navigateWithRetry(String url) {
        logger.info("Navigating to: {}", url);
        
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= MAX_NAVIGATION_RETRIES; attempt++) {
            try {
                logger.info("Navigation attempt #{} of {}", attempt, MAX_NAVIGATION_RETRIES);
                
                // Try direct navigation first
                driver.get(url);
                
                // Wait for page to be somewhat ready
                waitForPageReady();
                
                // If we get here, navigation succeeded
                logger.info("✅ Successfully navigated to: {}", driver.getCurrentUrl());
                return;
                
            } catch (TimeoutException e) {
                lastException = e;
                logger.warn("⚠️ Navigation timeout on attempt #{}", attempt);
                
                // Try to stop the page loading
                stopPageLoading();
                
                // Check if we're actually on the page despite timeout
                String currentUrl = driver.getCurrentUrl();
                if (currentUrl != null && currentUrl.contains("simtest")) {
                    logger.info("Page seems to be loaded despite timeout. Current URL: {}", currentUrl);
                    return;
                }
                
                if (attempt < MAX_NAVIGATION_RETRIES) {
                    logger.info("Waiting {} seconds before retry...", WAIT_BETWEEN_RETRIES / 1000);
                    sleep(WAIT_BETWEEN_RETRIES);
                }
                
            } catch (Exception e) {
                lastException = e;
                logger.error("❌ Error on attempt #{}: {}", attempt, e.getMessage());
                
                if (attempt < MAX_NAVIGATION_RETRIES) {
                    sleep(WAIT_BETWEEN_RETRIES);
                }
            }
        }
        
        // All retries failed, try JavaScript navigation as last resort
        logger.warn("All navigation attempts failed. Trying JavaScript navigation...");
        
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.stop();"); // Stop current loading
            sleep(1000);
            js.executeScript("window.location.href = arguments[0];", url);
            
            // Wait a bit for JS navigation
            sleep(10000);
            
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl != null && (currentUrl.contains("simtest") || currentUrl.contains("login"))) {
                logger.info("✅ JavaScript navigation successful. URL: {}", currentUrl);
                return;
            }
        } catch (Exception jsError) {
            logger.error("JavaScript navigation also failed: {}", jsError.getMessage());
        }
        
        // Log final error
        logger.error("========================================");
        logger.error("❌ NAVIGATION FAILED after all attempts");
        logger.error("   URL: {}", url);
        logger.error("   Last error: {}", lastException != null ? lastException.getMessage() : "Unknown");
        logger.error("   Possible causes:");
        logger.error("   1. Slow network connection");
        logger.error("   2. SIMTest server is slow or down");
        logger.error("   3. Firewall/proxy blocking the connection");
        logger.error("   4. Try running the test again");
        logger.error("========================================");
        
        // Continue anyway - some pages may still work
        logger.warn("Continuing with test execution despite navigation issues...");
    }
    
    /**
     * Stop page loading using JavaScript
     */
    private void stopPageLoading() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.stop();");
            logger.debug("Page loading stopped");
        } catch (Exception e) {
            logger.debug("Could not stop page loading: {}", e.getMessage());
        }
    }
    
    /**
     * Wait for page to be ready (document.readyState)
     */
    private void waitForPageReady() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(d -> {
                JavascriptExecutor js = (JavascriptExecutor) d;
                String state = (String) js.executeScript("return document.readyState");
                return "complete".equals(state) || "interactive".equals(state);
            });
        } catch (Exception e) {
            logger.debug("Page ready wait timed out: {}", e.getMessage());
        }
    }
    
    /**
     * Sleep helper
     */
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setupMethod() {
        logger.debug("Starting test method on thread: {}", Thread.currentThread().getName());
    }

    @AfterMethod(alwaysRun = true)
    public void teardownMethod() {
        logger.debug("Finished test method on thread: {}", Thread.currentThread().getName());
    }

    @AfterSuite(alwaysRun = true)
    public void teardownSuite() {
        logger.info("========================================");
        logger.info("Finishing Test Suite");
        logger.info("========================================");

        // Clear test context
        TestContext.clear();
        
        // Quit driver
        if (driver != null) {
            DriverManager.quitDriver();
            driver = null;
        }
    }

    /**
     * Get the WebDriver instance
     */
    protected WebDriver getDriver() {
        return driver;
    }

    /**
     * Get test context data
     */
    protected TestContext.TestData getTestData() {
        return TestContext.getData();
    }
}

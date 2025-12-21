package Pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * AccessShofhaPortal - Page Object for handling browser tabs/windows
 */
public class accessShofhaPortal extends BasePage {
    
    private static final String SHOFHA_URL = "https://shofha.com/";

    public accessShofhaPortal(WebDriver driver) {
        super(driver);
    }

    // ============ Actions ============

    /**
     * Open Shofha website in a new browser tab
     */
    public void openShofhaInNewTab() {
        openUrlInNewTab(SHOFHA_URL);
    }

    /**
     * Open any URL in a new browser tab
     * 
     * @param url The URL to open in new tab
     */
    public void openUrlInNewTab(String url) {
        try {
            logger.info("Opening URL in new tab: {}", url);
            
            // Open new tab using JavaScript
            executeScript("window.open()");
            
            // Get all window handles and switch to the last one
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabs.size() - 1));
            
            logger.info("Switched to new tab (total tabs: {})", tabs.size());
            
            // Navigate to URL
            driver.get(url);
            
            // Wait for page to load
            waitForPageStability();
            
            String currentUrl = driver.getCurrentUrl();
            logger.info("✅ Page opened successfully: {}", currentUrl);
            
        } catch (Exception e) {
            logger.error("❌ Failed to open URL in new tab: {}", url);
            logger.error("   Error: {}", e.getMessage());
            Assert.fail("Cannot open URL in new tab: " + url + " - " + e.getMessage());
        }
    }

    /**
     * Switch back to the SIMTest tab (first tab)
     */
    public void switchBackToSimtestTab() {
        try {
            logger.info("Switching back to SIMTest tab...");
            
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            
            if (tabs.isEmpty()) {
                throw new RuntimeException("No browser tabs available");
            }
            
            // Switch to first tab (SIMTest)
            driver.switchTo().window(tabs.get(0));
            
            // Wait for page to be ready
            waitForPageStability();
            
            logger.info("Switched back to SIMTest tab");
        } catch (Exception e) {
            logger.error("Failed to switch to SIMTest tab: {}", e.getMessage());
            Assert.fail("Cannot switch back to SIMTest tab: " + e.getMessage());
        }
    }

    /**
     * Switch from SIMTest tab to Shofha tab (second tab)
     */
    public void switchFromSimtestTabToShofhaTab() {
        try {
            logger.info("Switching to Shofha tab...");
            
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            
            if (tabs.size() < 2) {
                throw new RuntimeException("Shofha tab not found. Available tabs: " + tabs.size());
            }
            
            // Switch to second tab (Shofha)
            driver.switchTo().window(tabs.get(1));
            
            // Wait for page to be ready
            waitForPageStability();
            
            logger.info("Switched to Shofha tab");
        } catch (Exception e) {
            logger.error("Failed to switch to Shofha tab: {}", e.getMessage());
            Assert.fail("Cannot switch to Shofha tab: " + e.getMessage());
        }
    }

    /**
     * Close current tab and switch to another
     */
    public void closeCurrentTabAndSwitchTo(int tabIndex) {
        try {
            driver.close();
            
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            if (tabIndex < tabs.size()) {
                driver.switchTo().window(tabs.get(tabIndex));
                logger.info("Closed tab and switched to tab index: {}", tabIndex);
            }
        } catch (Exception e) {
            logger.error("Failed to close tab: {}", e.getMessage());
        }
    }

    /**
     * Get number of open tabs
     */
    public int getNumberOfOpenTabs() {
        Set<String> handles = driver.getWindowHandles();
        return handles.size();
    }

    /**
     * Switch to tab by index
     */
    public void switchToTab(int index) {
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (index >= 0 && index < tabs.size()) {
            driver.switchTo().window(tabs.get(index));
            waitForPageStability();
            logger.info("Switched to tab index: {}", index);
        } else {
            logger.warn("Invalid tab index: {}. Available: {}", index, tabs.size());
        }
    }
}

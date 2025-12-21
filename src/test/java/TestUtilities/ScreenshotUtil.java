package TestUtilities;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ScreenshotUtil - Utility for capturing screenshots
 */
public class ScreenshotUtil {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtil.class);
    private static final String SCREENSHOT_PATH = "test-output/screenshots/";

    /**
     * Take a screenshot and save it with test name and timestamp
     */
    public static String takeScreenshot(WebDriver driver, String testName) {
        if (driver == null) {
            logger.warn("Cannot take screenshot - driver is null");
            return null;
        }

        try {
            // Create screenshot directory if it doesn't exist
            File screenshotDir = new File(SCREENSHOT_PATH);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate filename with timestamp
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_PATH + fileName;

            // Take screenshot
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);

            logger.info("📸 Screenshot saved: {}", filePath);
            return filePath;

        } catch (Exception e) {
            logger.error("❌ Failed to take screenshot: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Take screenshot and return as Base64 string (for embedding in reports)
     */
    public static String takeScreenshotAsBase64(WebDriver driver) {
        if (driver == null) {
            return null;
        }

        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            logger.error("Failed to take Base64 screenshot: {}", e.getMessage());
            return null;
        }
    }
}

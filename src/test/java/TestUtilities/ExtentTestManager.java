package TestUtilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import java.util.HashMap;
import java.util.Map;

/**
 * ExtentTestManager - Thread-safe management of ExtentTest instances
 */
public class ExtentTestManager {
    private static final Map<Integer, ExtentTest> extentTestMap = new HashMap<>();
    private static final ExtentReports extent = ExtentManager.getInstance();

    /**
     * Get ExtentTest for current thread
     */
    public static synchronized ExtentTest getTest() {
        return extentTestMap.get((int) Thread.currentThread().getId());
    }

    /**
     * Start a new test with given name
     */
    public static synchronized ExtentTest startTest(String testName) {
        return startTest(testName, "");
    }

    /**
     * Start a new test with name and description
     */
    public static synchronized ExtentTest startTest(String testName, String description) {
        ExtentTest test = extent.createTest(testName, description);
        extentTestMap.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    /**
     * End test - removes from map
     */
    public static synchronized void endTest() {
        extentTestMap.remove((int) Thread.currentThread().getId());
    }

    /**
     * Get the ExtentReports instance
     */
    public static ExtentReports getExtentReports() {
        return extent;
    }
}

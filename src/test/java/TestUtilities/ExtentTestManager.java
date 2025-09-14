package TestUtilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static ExtentReports extent = ExtentManager.createInstance();

    // تبدأ Test جديد في التقرير
    public static synchronized ExtentTest startTest(String testName) {
        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);
        return test;
    }

    // تجيب الـ Test الحالي
    public static synchronized ExtentTest getTest() {
        return extentTest.get();
    }

    // تحفظ التقرير بعد ما التست كله يخلص
    public static synchronized void flush() {
        extent.flush();
    }
}
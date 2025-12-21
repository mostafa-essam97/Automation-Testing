package TestUtilities;

import Utilities.EmailReportSender;
import Utilities.TestContext;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListener - TestNG listener for test events and reporting
 */
public class TestListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("🚀 Starting test: {}", result.getMethod().getMethodName());
        ExtentTestManager.startTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("✅ Test PASSED: {}", result.getMethod().getMethodName());
        ExtentTestManager.getTest().log(Status.PASS, "Test Passed");

        logTestInfo();
        logTestSpecificInfo(result, true);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("❌ Test FAILED: {}", result.getMethod().getMethodName());
        
        // Take screenshot
        WebDriver driver = DriverManager.getDriver();
        if (driver != null) {
            String screenshotPath = ScreenshotUtil.takeScreenshot(driver, result.getName());
            ExtentTestManager.getTest().fail("Test Failed - Screenshot: " +
                    ExtentTestManager.getTest().addScreenCaptureFromPath(screenshotPath));
        } else {
            ExtentTestManager.getTest().fail("Test Failed: " + result.getThrowable().getMessage());
        }

        logTestInfo();
        logTestSpecificInfo(result, false);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⏭️ Test SKIPPED: {}", result.getMethod().getMethodName());
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped: " + result.getThrowable());
        logTestInfo();
    }

    @Override
    public void onStart(ITestContext context) {
        logger.info("========================================");
        logger.info("🏁 Test Suite Started: {}", context.getName());
        logger.info("========================================");
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========================================");
        logger.info("🏁 Test Suite Finished: {}", context.getName());
        logger.info("   Total: {} | Passed: {} | Failed: {} | Skipped: {}",
                context.getAllTestMethods().length,
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
        logger.info("========================================");

        // Flush report
        ExtentManager.getInstance().flush();

        // Email is now sent by HtmlReportListener (HTML Table format)
        // ExtentReport Dashboard email disabled:
        // String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";
        // EmailReportSender.sendReportByEmail(reportPath);
    }

    // ============ Private Helper Methods ============

    private void logTestInfo() {
        logSimtestLoginInfo();
        logReservationInfo();
        logOTPInfo();
    }

    private void logSimtestLoginInfo() {
        TestContext.TestData data = TestContext.getData();
        String username = data.getSimtestUsername();
        
        if (username != null) {
            ExtentTestManager.getTest().log(Status.INFO,
                    "👤 SimTest Login - Username: <b>" + username + "</b>");
            ExtentTestManager.getTest().log(Status.INFO,
                    "🔒 SimTest Password: ******");
        } else {
            ExtentTestManager.getTest().log(Status.INFO,
                    "⚠️ No SimTest login info for this test");
        }
    }

    private void logReservationInfo() {
        TestContext.TestData data = TestContext.getData();
        String number = data.getFullReservedNumber();
        String country = data.getCountryCode();
        
        if (number != null && country != null) {
            ExtentTestManager.getTest().log(Status.INFO,
                    "📱 Reserved Number: " + data.getReservedNumberText());
        } else {
            ExtentTestManager.getTest().log(Status.INFO,
                    "⚠️ No reservation data for this test");
        }
    }

    private void logOTPInfo() {
        TestContext.TestData data = TestContext.getData();
        String otp = data.getOtpCode();
        
        if (otp != null) {
            ExtentTestManager.getTest().log(Status.INFO,
                    "🔑 OTP Used: <b>" + otp + "</b>");
        } else {
            ExtentTestManager.getTest().log(Status.INFO,
                    "⚠️ No OTP captured for this test");
        }
    }

    private void logPackageInfo() {
        TestContext.TestData data = TestContext.getData();
        String packageType = data.getPackageType();
        String packagePrice = data.getPackagePrice();
        
        if (packageType != null) {
            ExtentTestManager.getTest().log(Status.INFO,
                    "📦 Package: " + packageType + " - Price: " + packagePrice);
        }
    }

    private void logTestSpecificInfo(ITestResult result, boolean passed) {
        String methodName = result.getMethod().getMethodName();
        
        switch (methodName) {
            case "openShofhaAndSubscribe":
            case "openShofha":
                logPackageInfo();
                break;

            case "verifyPortalOTP":
            case "PortalOTPCode":
            case "otpVerification":
                if (passed) {
                    ExtentTestManager.getTest().log(Status.PASS,
                            "🎉 User Subscription completed successfully!");
                } else {
                    ExtentTestManager.getTest().log(Status.FAIL,
                            "❌ User Subscription failed!");
                }
                logPackageInfo();
                break;

            case "cancelUserSubscription":
            case "userCancelSubscription":
                if (passed) {
                    ExtentTestManager.getTest().log(Status.PASS,
                            "✅ User Subscription cancelled successfully!");
                } else {
                    ExtentTestManager.getTest().log(Status.FAIL,
                            "⚠️ User Subscription cancellation failed!");
                }
                logPackageInfo();
                break;

            default:
                // No additional logging needed
                break;
        }
    }
}

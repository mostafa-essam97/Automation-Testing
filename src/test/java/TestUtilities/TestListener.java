package TestUtilities;

import Utilities.testDataHolder;
import Utilities.EmailReportSender;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        // بيشتغل أول ما التست يبدأ
        ExtentTestManager.startTest(result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test Passed");

        logSimtestLoginInfo(); // اضافه معلومات تسجيل دخول ل SimTest
        logReservationInfo(); // إضافة الرقم والبلد
        logOTPInfo();  // اضافه معلومات ال OTP

        // ✅ نتحقق من اسم التست ونضيف الرسالة المخصصة
        switch (result.getMethod().getMethodName()) {
            case "openShofha":
                userPackageInfo();
                break;

            case "otpVerification":
                logSubscriptionSuccess();
                userPackageInfo();
                break;

            case "userCancelSubscription":
                logCancellationSuccess();
                userPackageInfo();
                break;

            default:
                // باقي التستات مش محتاجة رسائل إضافية
                break;
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        WebDriver driver = DriverManager.getDriver(null);
        String screenshotPath = ScreenshotUtil.takeScreenshot(driver, result.getName());
        ExtentTestManager.getTest().fail("Test Failed, Screenshot attached: " +
                ExtentTestManager.getTest().addScreenCaptureFromPath(screenshotPath));

        logSimtestLoginInfo(); // اضافه معلومات تسجيل دخول ل SimTest
        logReservationInfo(); // إضافة الرقم والبلد
        logOTPInfo();  // اضافه معلومات ال OTP

        // ✅ Custom Failure Logs
        if (result.getMethod().getMethodName().equals("otpVerification")) {
            logSubscriptionFailure();
            userPackageInfo();
        } else if (result.getMethod().getMethodName().equals("userCancelSubscription")) {
            logCancellationFailure();
            userPackageInfo();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped: " + result.getThrowable());

        logSimtestLoginInfo(); // اضافه معلومات تسجيل دخول ل SimTest
        logReservationInfo(); // إضافة الرقم والبلد
        logOTPInfo();  // اضافه معلومات ال OTP

    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite Started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Test Suite Finished: " + context.getName());

        // بعد ما يتعمل flush للـ report
        ExtentManager.getInstance().flush();

        // المسار بتاع التقرير
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";

        // ننده على كلاس الميل
        EmailReportSender.sendReportByEmail(reportPath);
    }

    private void logSimtestLoginInfo() {
        if (testDataHolder.simtestUsernameData != null) {
            ExtentTestManager.getTest().log(Status.INFO,
                    "👤 SimTest Login - Username: <b>" + testDataHolder.simtestUsernameData + "</b>");

            if (testDataHolder.simtestPasswordData != null) {
                ExtentTestManager.getTest().log(Status.INFO,
                        "🔒 SimTest Password: ******"); // Masked
            }
        } else {
            ExtentTestManager.getTest().log(Status.INFO,
                    "⚠️ No SimTest login info found for this test.");
        }
    }



    // ميثود خاصة بتسجيل الرقم والبلد في التقرير
    private void logReservationInfo() {
        if (testDataHolder.fullReservedNumberData != null && testDataHolder.countryCodeData != null) {
            ExtentTestManager.getTest().log(Status.INFO,
                    "📱 Reserved Number: " + testDataHolder.reservedNumberTextData);
        } else {
            ExtentTestManager.getTest().log(Status.INFO,
                    "⚠️ No reservation data found for this test.");
        }
    }

    private void logOTPInfo() {
        if (testDataHolder.otpCodeData != null) {
            ExtentTestManager.getTest().log(Status.INFO,
                    "🔑 OTP Used: <b>" + testDataHolder.otpCodeData + "</b>");
        } else {
            ExtentTestManager.getTest().log(Status.INFO,
                    "⚠️ No OTP captured for this test.");
        }
    }

    private void userPackageInfo(){
        ExtentTestManager.getTest().log(Status.PASS,
                "The user's subscription package is: " + testDataHolder.packageTypeData + " With price: " + testDataHolder.packagePriceData);
    }

    private void logSubscriptionSuccess() {
        ExtentTestManager.getTest().log(Status.PASS,
                "🎉 User Subscription completed successfully!");
    }

    private void logCancellationSuccess() {
        ExtentTestManager.getTest().log(Status.PASS,
                "✅ User Subscription cancelled successfully!");
    }

    private void logSubscriptionFailure() {
        ExtentTestManager.getTest().log(Status.FAIL,
                "❌ User Subscription failed!");
    }

    private void logCancellationFailure() {
        ExtentTestManager.getTest().log(Status.FAIL,
                "⚠️ User Subscription cancellation failed!");
    }

}
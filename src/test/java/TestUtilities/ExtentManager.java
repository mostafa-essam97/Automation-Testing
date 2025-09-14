package TestUtilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getInstance() {
        if (extent == null) {
            createInstance();
        }
        return extent;
    }

    public static ExtentReports createInstance() {
        String reportPath = System.getProperty("user.dir") + "/test-output/ExtentReport.html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Automation Test Report");
        spark.config().setReportName("Shofha Regression Suite Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        // معلومات إضافية تظهر في التقرير
        extent.setSystemInfo("Project", "Shofha");
        extent.setSystemInfo("Tester", "Mostafa Essam");
        extent.setSystemInfo("Environment", "QA");

        return extent;
    }
}
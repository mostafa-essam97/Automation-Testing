package TestUtilities;

import Utilities.ReportDataModel;
import Utilities.testDataHolder;
import Utilities.HtmlReportBuilder;
import Utilities.EmailReportSender;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import java.util.ArrayList;
import java.util.List;

public class HtmlReportListener implements ITestListener {

    private static List<ReportDataModel> reportDataList = new ArrayList<>();

    @Override
    public void onTestSuccess(ITestResult result) {
        addReportData(result, "PASS");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        addReportData(result, "FAIL");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        addReportData(result, "SKIPPED");
    }

    @Override
    public void onFinish(ITestContext context) {
        // بعد ما التيست كلها تخلص
        String htmlContent = HtmlReportBuilder.build(reportDataList);
        EmailReportSender.sendEmail("Automation HTML Report", htmlContent);
    }

    private void addReportData(ITestResult result, String status) {
        ReportDataModel model = new ReportDataModel();

        // Execution Data
        model.setTestCaseName(result.getMethod().getMethodName());
        model.setClassName(result.getTestClass().getName());
        model.setStatus(status);
        model.setDuration((result.getEndMillis() - result.getStartMillis()) + " ms");

        // Business Data من testDataHolder
        model.setUsername(testDataHolder.simtestUsernameData);
        model.setReservedNumberText(testDataHolder.reservedNumberTextData);
        model.setFullReservedNumber(testDataHolder.fullReservedNumberData);
        model.setCountryCode(testDataHolder.countryCodeData);
        model.setPackageType(testDataHolder.packageTypeData);
        model.setPackagePrice(testDataHolder.packagePriceData);
        model.setSubscriptionTimestamp(testDataHolder.subscriptionTimeStampData);
        model.setOtpCode(testDataHolder.otpCodeData);

        reportDataList.add(model);
    }
}
package TestCases;

import Pages.accessShofhaPortal;
import Pages.verifySubscriptionPage;
import Utilities.TestContext;
import org.testng.annotations.Test;

/**
 * TC05_VerifyPortalOTP - Test case for OTP verification on Shofha portal
 */
public class TC05_VerifyPortalOTP extends testBase {

    @Test(description = "Verify OTP code on Shofha portal to complete subscription")
    public void verifyPortalOTP() {
        accessShofhaPortal portal = new accessShofhaPortal(driver);
        verifySubscriptionPage verifyPage = new verifySubscriptionPage(driver);

        // Get OTP from test context
        String otpCode = TestContext.getData().getOtpCode();

        // Switch to Shofha tab
        portal.switchFromSimtestTabToShofhaTab();
        
        // Enter OTP and verify
        verifyPage.insertPinCode(otpCode);
    }
}







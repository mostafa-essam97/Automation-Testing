package TestCases;

import Pages.accessShofhaPortal;
import Pages.simTestMessages;
import Pages.verifySubscriptionPage;
import Utilities.testDataHolder;
import org.testng.annotations.Test;

public class TC_05_VerifyPortalOTP extends testBase{
    @Test
    public void PortalOTPCode(){
        verifySubscriptionPage verify = new verifySubscriptionPage(driver);
        accessShofhaPortal portal = new accessShofhaPortal(driver);

        portal.switchFromSimtestTabToShofhaTap();
        verify.insertPinCode(testDataHolder.otpCodeData);
        verify.accessVerifyBtn();
    }
}

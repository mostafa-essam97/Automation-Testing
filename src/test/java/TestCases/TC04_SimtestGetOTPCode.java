package TestCases;

import Pages.SimtestMessagesPage;
import org.testng.annotations.Test;

/**
 * TC04_SimtestGetOTPCode - Test case for extracting OTP from SIMTest messages
 */
public class TC04_SimtestGetOTPCode extends testBase {

    @Test(description = "Get OTP code from SIMTest SMS messages")
    public void getOTPFromSimtest() {
        SimtestMessagesPage messagesPage = new SimtestMessagesPage(driver);

        // Find sender and extract OTP
        messagesPage.findSenderMessage();
        messagesPage.extractOTPFromAllSenders();
    }
}







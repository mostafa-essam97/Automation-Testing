package TestCases;

import Pages.accessShofhaPortal;
import Pages.simTestMessages;
import org.testng.annotations.Test;

public class TC_04_SimTestGetOTPCode extends testBase{
    @Test
    public void getOTPfromSimtest(){
        simTestMessages message = new simTestMessages(driver);

        message.findSenderMessage();
        message.extractOTPFromAllSenders();

    }
}

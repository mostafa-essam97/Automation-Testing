package TestCases;

import Pages.simtestLoginPage;
import Utilities.testDataHolder;
import org.testng.annotations.Test;

public class TC_01_SIMTESTLogin extends testBase {
    @Test
    public void loginToSIMTestWebsite() {
        simtestLoginPage log = new simtestLoginPage(driver);
        testDataHolder.simtestUsernameData = "arpuplus";
        testDataHolder.simtestPasswordData = "domain.arpu@";
        log.simTestlogin("arpuplus" , "domain.arpu@");
    }
}
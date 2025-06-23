package TestCases;

import Pages.accessShofhaPortal;
import Pages.shofhaHomePage;
import Pages.simtestLoginPage;
import org.testng.annotations.Test;

public class TC_SIMTestLogin extends testBase {
    @Test
    public void accessSIMTestWebsite() {
        simtestLoginPage log = new simtestLoginPage(driver);
        accessShofhaPortal shofha = new accessShofhaPortal(driver);
        shofhaHomePage shofhaHome = new shofhaHomePage(driver);

        log.simTestlogin("arpuplus" , "domain.arpu@");
        shofha.openShofhaInNewTap();
        shofha.switchBackToSimtestTab();
        shofha.switchFromSimtestTabToShofhaTap();
        shofhaHome.openSubscriptionPage();
    }
}
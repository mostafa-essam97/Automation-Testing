package TestCases;

import Pages.*;
import org.testng.annotations.Test;
import Utilities.testDataHolder;

public class TC_03_OpenShofhaAndSubscribe extends testBase {
    @Test
    public void openShofha() {
        accessShofhaPortal portal = new accessShofhaPortal(driver);
        shofhaHomePage shofhaHome = new shofhaHomePage(driver);
        shofhaSubscriptionPage sub = new shofhaSubscriptionPage(driver);
        shofhaLP lp = new shofhaLP(driver);
        shofhaPackages packages = new shofhaPackages(driver);

        portal.openShofhaInNewTab();
        shofhaHome.openSubscriptionPage();
        sub.enterReservedNumber(testDataHolder.fullReservedNumberData);
        sub.selectCountryByCode(testDataHolder.countryCodeData);
        sub.accessNewLPPage();
        lp.navigateToPackegesPage();
        packages.selectPackage(0);
        portal.switchBackToSimtestTab();
    }
}

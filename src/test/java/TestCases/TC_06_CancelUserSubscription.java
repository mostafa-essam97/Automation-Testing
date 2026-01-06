package TestCases;

import Pages.shofhaAccountSettingPage;
import Pages.shofhaHomePage;
import org.testng.annotations.Test;

public class TC_06_CancelUserSubscription extends testBase{
    @Test
    public void userCancelSubscription(){
        shofhaHomePage homePage = new shofhaHomePage(driver);
        shofhaAccountSettingPage setting = new shofhaAccountSettingPage(driver);

        homePage.openUserProfileIcon();
        homePage.openUserAccountSettings();
        setting.accessSubscriptionDetails();
        setting.CancelSubscription();
        setting.confirmCancellation();
    }
}
package TestCases;

import Pages.ShofhaAccountSettingsPage;
import Pages.shofhaHomePage;
import org.testng.annotations.Test;

/**
 * TC06_CancelUserSubscription - Test case for canceling user subscription
 */
public class TC06_CancelUserSubscription extends testBase {

    @Test(description = "Cancel user subscription from Shofha account settings")
    public void cancelUserSubscription() {
        shofhaHomePage homePage = new shofhaHomePage(driver);
        ShofhaAccountSettingsPage settingsPage = new ShofhaAccountSettingsPage(driver);

        // Navigate to account settings
        homePage.openUserProfileIcon();
        homePage.openUserAccountSettings();
        
        // Complete cancellation
        settingsPage.completeCancellation();
    }
}





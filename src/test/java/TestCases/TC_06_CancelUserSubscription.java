package TestCases;

import Pages.shofhaAccountSettingPage;
import Pages.shofhaHomePage;
import Utilities.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TC_06_CancelUserSubscription extends testBase {
    private static final Logger logger = LoggerFactory.getLogger(TC_06_CancelUserSubscription.class);

    @Test
    public void userCancelSubscription() {
        logger.info("========================================");
        logger.info("Starting TC_06: Cancel User Subscription");
        logger.info("========================================");

        shofhaHomePage homePage = new shofhaHomePage(driver);
        shofhaAccountSettingPage setting = new shofhaAccountSettingPage(driver);

        // Log current test data for reference
        TestContext.TestData data = TestContext.getData();
        logger.info("Test context data:");
        logger.info("   Reserved Number: {}", data.getFullReservedNumber());
        logger.info("   Package Type: {}", data.getPackageType());

        // Step 1: Open profile icon
        logger.info("Step 1: Opening user profile icon...");
        homePage.openUserProfileIcon();

        // Step 2: Open account settings
        logger.info("Step 2: Opening account settings...");
        homePage.openUserAccountSettings();

        // Step 3: Access subscription details
        logger.info("Step 3: Accessing subscription details...");
        setting.accessSubscriptionDetails();

        // Step 4: Cancel subscription
        logger.info("Step 4: Cancelling subscription...");
        setting.CancelSubscription();

        // Step 5: Confirm cancellation
        logger.info("Step 5: Confirming cancellation...");
        setting.confirmCancellation();

        logger.info("✅ TC_06 completed successfully!");
        logger.info("   Subscription cancelled for number: {}", data.getFullReservedNumber());
    }
}
package TestCases;

import Pages.accessShofhaPortal;
import Pages.shofhaSubscriptionPage;
import Pages.shofhaPackages;
import Utilities.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * TC03_OpenShofhaAndSubscribe - Test case for Shofha subscription flow
 * Uses the NEW subscription landing page
 */
public class TC03_OpenShofhaAndSubscribe extends testBase {
    private static final Logger logger = LoggerFactory.getLogger(TC03_OpenShofhaAndSubscribe.class);
    
    // Shofha subscription landing page URL
    private static final String SUBSCRIPTION_URL = "https://subscription.shofha.com/subscriptionLandingPage/Web/DCB?lang=ar";

    @Test(description = "Open Shofha portal and initiate subscription with reserved number")
    public void openShofhaAndSubscribe() {
        logger.info("========================================");
        logger.info("Starting TC03: Open Shofha and Subscribe");
        logger.info("========================================");

        // Initialize page objects
        accessShofhaPortal portal = new accessShofhaPortal(driver);
        shofhaSubscriptionPage subscriptionPage = new shofhaSubscriptionPage(driver);
        shofhaPackages packagesPage = new shofhaPackages(driver);

        // Get test data from context
        TestContext.TestData data = TestContext.getData();
        String fullNumber = data.getFullReservedNumber();
        String countryCode = data.getCountryCode();

        // Validate data
        logger.info("Test data:");
        logger.info("   Full Number: {}", fullNumber);
        logger.info("   Country Code: {}", countryCode);

        if (fullNumber == null || fullNumber.isEmpty()) {
            logger.error("❌ Full reserved number is NULL!");
            Assert.fail("Full reserved number is null. TC02 must complete successfully first.");
        }

        if (countryCode == null || countryCode.isEmpty()) {
            logger.error("❌ Country code is NULL!");
            Assert.fail("Country code is null. TC02 must complete successfully first.");
        }

        // Step 1: Open Shofha subscription page in new tab
        logger.info("Step 1: Opening Shofha subscription page...");
        portal.openUrlInNewTab(SUBSCRIPTION_URL);
        
        // Step 2: Select country
        logger.info("Step 2: Selecting country: {}", countryCode);
        subscriptionPage.selectCountryByCode(countryCode);
        
        // Step 3: Enter phone number (without country code)
        logger.info("Step 3: Entering phone number...");
        subscriptionPage.enterReservedNumber(fullNumber, countryCode);
        
        // Step 4: Click Next
        logger.info("Step 4: Clicking Next button...");
        subscriptionPage.clickNextButton();
        
        // Step 5: Select cheapest package automatically
        logger.info("Step 5: Selecting cheapest package...");
        packagesPage.selectCheapestPackage();
        
        // Step 6: Switch back to SIMTest tab
        logger.info("Step 6: Switching back to SIMTest...");
        portal.switchBackToSimtestTab();

        logger.info("✅ TC03 completed successfully!");
    }
}


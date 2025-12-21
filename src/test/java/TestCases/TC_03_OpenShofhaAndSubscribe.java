package TestCases;

import Pages.*;
import Utilities.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_03_OpenShofhaAndSubscribe extends testBase {
    private static final Logger logger = LoggerFactory.getLogger(TC_03_OpenShofhaAndSubscribe.class);

    // Shofha subscription landing page URL
    private static final String SUBSCRIPTION_URL = "https://subscription.shofha.com/subscriptionLandingPage/Web/DCB?lang=ar";

    @Test
    public void openShofha() {
        logger.info("========================================");
        logger.info("Starting TC_03: Open Shofha and Subscribe");
        logger.info("========================================");

        accessShofhaPortal portal = new accessShofhaPortal(driver);
        shofhaSubscriptionPage sub = new shofhaSubscriptionPage(driver);
        shofhaPackages packages = new shofhaPackages(driver);

        // Get data from TestContext
        TestContext.TestData testData = TestContext.getData();
        String fullReservedNumber = testData.getFullReservedNumber();
        String countryCode = testData.getCountryCode();

        // Validate data before proceeding
        logger.info("Validating test data from previous test...");
        logger.info("   Full Reserved Number: {}", fullReservedNumber);
        logger.info("   Country Code: {}", countryCode);

        if (fullReservedNumber == null || fullReservedNumber.isEmpty()) {
            logger.error("❌ ERROR: Full reserved number is NULL or empty!");
            logger.error("   This means TC_02 (Reservation) did not complete successfully.");
            logger.error("   Check that TC_02 ran before this test and stored the number correctly.");
            Assert.fail("Full reserved number is null. TC_02 must complete successfully before TC_03.");
        }

        if (countryCode == null || countryCode.isEmpty()) {
            logger.error("❌ ERROR: Country code is NULL or empty!");
            Assert.fail("Country code is null. TC_02 must complete successfully before TC_03.");
        }

        // Step 1: Open Shofha subscription page in new tab
        logger.info("Step 1: Opening Shofha subscription page in new tab...");
        logger.info("   URL: {}", SUBSCRIPTION_URL);
        portal.openUrlInNewTab(SUBSCRIPTION_URL);

        // Step 2: Select country from dropdown
        logger.info("Step 2: Selecting country by code: {}", countryCode);
        sub.selectCountryByCode(countryCode);

        // Step 3: Enter phone number (without country code)
        logger.info("Step 3: Entering reserved number...");
        logger.info("   Full number: {}", fullReservedNumber);
        logger.info("   Country code to remove: {}", countryCode);
        sub.enterReservedNumber(fullReservedNumber, countryCode);

        // Step 4: Click Next button to proceed
        logger.info("Step 4: Clicking Next button...");
        sub.clickNextButton();

        // Step 5: Select cheapest subscription package automatically
        logger.info("Step 5: Selecting cheapest package automatically...");
        packages.selectCheapestPackage();

        // Step 6: Switch back to SIMTest tab to get OTP
        logger.info("Step 6: Switching back to SIMTest tab...");
        portal.switchBackToSimtestTab();

        logger.info("✅ TC_03 completed successfully!");
        logger.info("   Subscription initiated for number: {}", fullReservedNumber);
        logger.info("   Package selected, waiting for OTP...");
    }
}

package TestCases;

import Pages.accessShofhaPortal;
import Pages.verifySubscriptionPage;
import Utilities.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_05_VerifyPortalOTP extends testBase {
    private static final Logger logger = LoggerFactory.getLogger(TC_05_VerifyPortalOTP.class);

    @Test
    public void PortalOTPCode() {
        logger.info("========================================");
        logger.info("Starting TC_05: Verify Portal OTP");
        logger.info("========================================");

        verifySubscriptionPage verify = new verifySubscriptionPage(driver);
        accessShofhaPortal portal = new accessShofhaPortal(driver);

        // Get OTP from TestContext
        String otpCode = TestContext.getData().getOtpCode();

        // Validate OTP before proceeding
        logger.info("Validating OTP from previous test...");
        logger.info("   OTP Code: {}", otpCode);

        if (otpCode == null || otpCode.isEmpty()) {
            logger.error("❌ ERROR: OTP code is NULL or empty!");
            logger.error("   This means TC_04 (Get OTP) did not complete successfully.");
            logger.error("   Check that TC_04 ran before this test and extracted the OTP correctly.");
            Assert.fail("OTP code is null. TC_04 must complete successfully before TC_05.");
        }

        // Step 1: Switch to Shofha tab
        logger.info("Step 1: Switching from SIMTest tab to Shofha tab...");
        portal.switchFromSimtestTabToShofhaTab();

        // Step 2: Insert OTP
        logger.info("Step 2: Inserting OTP code: {}", otpCode);
        verify.insertPinCode(otpCode);

        // Step 3: Click verify button
        logger.info("Step 3: Clicking verify button...");
        verify.clickVerifyButton();

        logger.info("✅ TC_05 completed successfully!");
        logger.info("   OTP verified: {}", otpCode);
    }
}

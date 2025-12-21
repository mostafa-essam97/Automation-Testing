package TestCases;

import Pages.simTestMessages;
import Utilities.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_04_SimTestGetOTPCode extends testBase {
    private static final Logger logger = LoggerFactory.getLogger(TC_04_SimTestGetOTPCode.class);

    @Test
    public void getOTPfromSimtest() {
        logger.info("========================================");
        logger.info("Starting TC_04: Get OTP from SIMTest");
        logger.info("========================================");

        simTestMessages message = new simTestMessages(driver);

        // Validate subscription timestamp exists
        String subscriptionTimestamp = TestContext.getData().getSubscriptionTimestamp();
        logger.info("Checking subscription timestamp: {}", subscriptionTimestamp);

        if (subscriptionTimestamp == null || subscriptionTimestamp.isEmpty()) {
            logger.error("❌ ERROR: Subscription timestamp is NULL!");
            logger.error("   This means TC_03 (Subscribe) did not complete successfully.");
            logger.error("   The timestamp is needed to filter messages received after subscription.");
            Assert.fail("Subscription timestamp is null. TC_03 must complete successfully before TC_04.");
        }

        // Step 1: Find sender message
        logger.info("Step 1: Finding sender message in SMS list...");
        message.findSenderMessage();

        // Step 2: Extract OTP
        logger.info("Step 2: Extracting OTP from messages...");
        message.extractOTPFromAllSenders();

        // Verify OTP was stored
        String otpCode = TestContext.getData().getOtpCode();
        if (otpCode != null && !otpCode.isEmpty()) {
            logger.info("✅ TC_04 completed successfully!");
            logger.info("   OTP Code extracted: {}", otpCode);
        } else {
            logger.error("❌ OTP extraction completed but no OTP was stored!");
            Assert.fail("OTP extraction failed - no OTP code stored in TestContext.");
        }
    }
}

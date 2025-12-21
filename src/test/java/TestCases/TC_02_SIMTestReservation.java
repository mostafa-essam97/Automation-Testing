package TestCases;

import Pages.*;
import Utilities.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_02_SIMTestReservation extends testBase {
    private static final Logger logger = LoggerFactory.getLogger(TC_02_SIMTestReservation.class);

    @Test
    public void accessReservations() {
        logger.info("========================================");
        logger.info("Starting TC_02: SIMTest Reservation");
        logger.info("========================================");

        simtestHomePage home = new simtestHomePage(driver);
        simtestSelectCountryPage reserve = new simtestSelectCountryPage(driver);
        simTestReserveNumbersPage num = new simTestReserveNumbersPage(driver);
        simTestReserveFreeSlotPage slot = new simTestReserveFreeSlotPage(driver);
        simTestActiveNumbersPage active = new simTestActiveNumbersPage(driver);

        // Step 1: Open reservations
        logger.info("Step 1: Opening reservations page...");
        home.openReservations();

        // Step 2: Select country
        logger.info("Step 2: Selecting country: United Arab Emirates");
        reserve.selectCountry("United Arab Emirates");

        // Step 3: Choose number
        logger.info("Step 3: Choosing number by index: 1");
        num.chooseNumberByIndex(1);

        // Step 4: Find and select free slot
        logger.info("Step 4: Finding free slots...");
        slot.clickFindFreeSlots();

        logger.info("Step 5: Selecting free slot at index: 2");
        slot.chooseFreeSlotByIndex(2);

        // Step 6: Navigate to SMS testing
        logger.info("Step 6: Opening testing tab...");
        home.openTestingTab();

        logger.info("Step 7: Opening SMS section...");
        home.openSMS();

        // Step 8: Verify reserved number - Use TestContext instead of testDataHolder
        String reservedNumber = TestContext.getData().getReservedNumberText();
        logger.info("Step 8: Verifying reserved number in active list: '{}'", reservedNumber);

        if (reservedNumber == null || reservedNumber.isEmpty()) {
            logger.error("❌ ERROR: Reserved number is NULL or empty!");
            logger.error("   This means the number was not stored correctly in previous steps.");
            logger.error("   Check simTestReserveNumbersPage.chooseNumberByIndex() method.");
            Assert.fail("Reserved number is null or empty. The number extraction/storage failed in previous steps.");
        }

        active.verifyReservedNumberInActiveList(reservedNumber);

        logger.info("✅ TC_02 completed successfully!");
        logger.info("   Reserved Number: {}", reservedNumber);
        logger.info("   Full Number: {}", TestContext.getData().getFullReservedNumber());
        logger.info("   Country Code: {}", TestContext.getData().getCountryCode());

        // { Bahrain, United Arab Emirates, Turkey, Spain, Senegal, Portugal, Oman } ==> Countries That has number for automation
    }
}
package TestCases;

import Pages.*;
import Utilities.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class General_TC_00_FullFlow extends testBase {
    private static final Logger logger = LoggerFactory.getLogger(General_TC_00_FullFlow.class);
    
    // Shofha subscription landing page URL
    private static final String SUBSCRIPTION_URL = "https://subscription.shofha.com/subscriptionLandingPage/Web/DCB?lang=ar";

    @Test
    public void accessReservations() {
        logger.info("========================================");
        logger.info("Starting Full Flow Test");
        logger.info("========================================");

        // Initialize page objects
        simtestLoginPage log = new simtestLoginPage(driver);
        simtestHomePage home = new simtestHomePage(driver);
        simtestSelectCountryPage reserve = new simtestSelectCountryPage(driver);
        simTestReserveNumbersPage num = new simTestReserveNumbersPage(driver);
        simTestReserveFreeSlotPage slot = new simTestReserveFreeSlotPage(driver);
        simTestActiveNumbersPage active = new simTestActiveNumbersPage(driver);
        accessShofhaPortal portal = new accessShofhaPortal(driver);
        shofhaSubscriptionPage sub = new shofhaSubscriptionPage(driver);
        shofhaPackages packages = new shofhaPackages(driver);
        simTestMessages message = new simTestMessages(driver);
        verifySubscriptionPage verify = new verifySubscriptionPage(driver);
        shofhaHomePage shofhaHome = new shofhaHomePage(driver);
        shofhaAccountSettingPage setting = new shofhaAccountSettingPage(driver);

        // { Bahrain, United Arab Emirates, Turkey, Spain, Senegal, Portugal, Oman } ==> Countries That has number for automation

        // ========== PART 1: SIMTest Login & Reservation ==========
        logger.info("PART 1: SIMTest Login & Reservation");
        log.login("arpuplus", "domain.arpu@");
        home.openReservations();
        reserve.selectCountry("Bahrain");
        num.chooseNumberByIndex(0);
        slot.chooseFreeSlotByIndex(1);
        slot.chooseFreeSlotByIndex(0);
        home.openTestingTab();
        home.openSMS();
        
        // Get data from TestContext
        TestContext.TestData testData = TestContext.getData();
        String reservedNumberText = testData.getReservedNumberText();
        String fullReservedNumber = testData.getFullReservedNumber();
        String countryCode = testData.getCountryCode();
        
        active.verifyReservedNumberInActiveList(reservedNumberText);

        // ========== PART 2: Shofha Subscription ==========
        logger.info("PART 2: Shofha Subscription");
        portal.openUrlInNewTab(SUBSCRIPTION_URL);
        sub.selectCountryByCode(countryCode);
        sub.enterReservedNumber(fullReservedNumber, countryCode);
        sub.clickNextButton();
        packages.selectCheapestPackage();

        // ========== PART 3: Get OTP from SIMTest ==========
        logger.info("PART 3: Get OTP from SIMTest");
        portal.switchBackToSimtestTab();
        message.findSenderMessage();
        message.extractOTPFromAllSenders();

        // ========== PART 4: Verify OTP on Shofha ==========
        logger.info("PART 4: Verify OTP on Shofha");
        portal.switchFromSimtestTabToShofhaTab();
        String otpCode = testData.getOtpCode();
        verify.insertPinCode(otpCode);
        verify.clickVerifyButton();

        // ========== PART 5: Cancel Subscription ==========
        logger.info("PART 5: Cancel Subscription");
        shofhaHome.openUserProfileIcon();
        shofhaHome.openUserAccountSettings();
        setting.accessSubscriptionDetails();
        
        logger.info("✅ Full Flow Test completed successfully!");
    }
}
package TestCases;

import Pages.*;
import org.testng.annotations.Test;
import Utilities.testDataHolder;


public class General_TC_00_FullFlow extends testBase {

    @Test
    public void accessReservations() {
        simtestLoginPage log = new simtestLoginPage(driver);
        simtestHomePage home = new simtestHomePage(driver);
        simtestSelectCountryPage reserve = new simtestSelectCountryPage(driver);
        simTestReserveNumbersPage num = new simTestReserveNumbersPage(driver);
        simTestReserveFreeSlotPage slot = new simTestReserveFreeSlotPage(driver);
        simTestActiveNumbersPage active = new simTestActiveNumbersPage(driver);
        accessShofhaPortal portal = new accessShofhaPortal(driver);
        shofhaHomePage shofhaHome = new shofhaHomePage(driver);
        shofhaSubscriptionPage sub = new shofhaSubscriptionPage(driver);
        shofhaLP lp = new shofhaLP(driver);
        shofhaPackages packages = new shofhaPackages(driver);
        simTestMessages message = new simTestMessages(driver);
        verifySubscriptionPage verify = new verifySubscriptionPage(driver);
        shofhaAccountSettingPage setting = new shofhaAccountSettingPage(driver);

        //{ Bahrain, United Arab Emirates, Turkey, Spain, Senegal, Portugal, Oman} ==> Countries That has number for automation

        log.simTestlogin("arpuplus", "domain.arpu@");
        home.openReservations();
        reserve.selectCountry("Bahrain");
        num.chooseNumberByIndex(0);
        slot.accessFreeSlotsBtn();
        slot.chooseFreeSlotByIndex(0);
        home.openTestingTap();
        home.openSMS();
        active.verifyReservedNumberInActiveList(testDataHolder.reservedNumberTextData);

        portal.openShofhaInNewTap();
        shofhaHome.openSubscriptionPage();
        sub.enterReservedNumber(testDataHolder.fullReservedNumberData);
        sub.selectCountryByCode(testDataHolder.countryCodeData);
        sub.accessNewLPPage();
        lp.navigateToPackegesPage();
        packages.selectPackage(0);

        portal.switchBackToSimtestTab();
        message.findSenderMessage();
        message.extractOTPFromAllSenders();

        portal.switchFromSimtestTabToShofhaTap();
        verify.insertPinCode(message.OTP);
        verify.accessVerifyBtn();

        shofhaHome.openUserProfileIcon();
        shofhaHome.openUserAccountSetting();
        setting.accessSubscriptionDetails();
    }
}
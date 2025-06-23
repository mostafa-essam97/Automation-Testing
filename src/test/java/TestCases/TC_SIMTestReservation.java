package TestCases;

import Pages.*;
import org.testng.annotations.Test;

public class TC_SIMTestReservation extends testBase{
    @Test
    public void accessReservations(){
        simtestLoginPage log = new simtestLoginPage(driver);
        simtestHomePage home = new simtestHomePage(driver);
        simtestReservationPage reserve = new simtestReservationPage(driver);
        accessShofhaPortal portal = new accessShofhaPortal(driver);
        shofhaHomePage shofhaHome = new shofhaHomePage(driver);
        shofhaSubscriptionPage sub  = new shofhaSubscriptionPage(driver);
        shofhaLP lp = new shofhaLP(driver);
        shofhaPackages packages = new shofhaPackages(driver);
        simTestMessages message = new simTestMessages(driver);
        verifySubscriptionPage verify = new verifySubscriptionPage(driver);


        log.simTestlogin("arpuplus" , "domain.arpu@");
        home.openReservations();
        reserve.selectCountry("Bahrain");
        reserve.chooseNumberByIndex(0);
        reserve.accessFreeSlotsBtn();
        reserve.chooseFreeSlotByIndex(2);
        home.openTestingTap();
        home.openSMS();
        reserve.verifyReservedNumberInActiveList(reserve.reservedNumberText);
        portal.openShofhaInNewTap();
        shofhaHome.openSubscriptionPage();
        sub.enterReservedNumber(reserve.getFullReservedNumber());
        sub.selectCountryByCode(reserve.getReservedNumbercountryCode());
        sub.accessNewLPPage();
        lp.navigateToPackegesPage();
        packages.selectPackage(0);
        portal.switchBackToSimtestTab();
        message.findSenderMessage();
        message.extractOTP(packages.subscriptionTimestamp);
        portal.switchFromSimtestTabToShofhaTap();
        verify.insertPinCode(message.OTP);
        verify.accessVerifyBtn();

        //{ Bahrain, United Arab Emirates, Turkey, Spain, Senegal, Portugal, Oman} ==> Countries That has number for automation

    }
}
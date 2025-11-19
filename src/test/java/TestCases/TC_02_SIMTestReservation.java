package TestCases;

import Pages.*;
import Utilities.testDataHolder;
import org.testng.annotations.Test;

public class TC_02_SIMTestReservation extends testBase{
    @Test
    public void accessReservations(){
        simtestHomePage home = new simtestHomePage(driver);
        simtestSelectCountryPage reserve = new simtestSelectCountryPage(driver);
        simTestReserveNumbersPage num = new simTestReserveNumbersPage(driver);
        simTestReserveFreeSlotPage slot = new simTestReserveFreeSlotPage(driver);
        simTestActiveNumbersPage active = new simTestActiveNumbersPage(driver);


        home.openReservations();
        reserve.selectCountry("Bahrain");
        num.chooseNumberByIndex(0);
        slot.accessFreeSlotsBtn();
        slot.chooseFreeSlotByIndex(2);
        home.openTestingTap();
        home.openSMS();
        active.verifyReservedNumberInActiveList(testDataHolder.reservedNumberTextData);


        //{ Bahrain, United Arab Emirates, Turkey, Spain, Senegal, Portugal, Oman} ==> Countries That has number for automation

    }
}
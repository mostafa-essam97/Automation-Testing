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
        reserve.selectCountry("United Arab Emirates");
        num.chooseNumberByIndex(1);
        slot.clickFindFreeSlots();
        slot.chooseFreeSlotByIndex(2);
        home.openTestingTab();
        home.openSMS();
        active.verifyReservedNumberInActiveList(testDataHolder.reservedNumberTextData);


        //{ Bahrain, United Arab Emirates, Turkey, Spain, Senegal, Portugal, Oman} ==> Countries That has number for automation

    }
}
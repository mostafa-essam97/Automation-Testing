package TestCases;

import Pages.simtestHomePage;
import Pages.simtestSelectCountryPage;
import Pages.simTestReserveNumbersPage;
import Pages.simTestReserveFreeSlotPage;
import Pages.simTestActiveNumbersPage;
import Utilities.TestContext;
import org.testng.annotations.Test;

/**
 * TC02_SimtestReservation - Test case for number reservation in SIMTest
 */
public class TC02_SimtestReservation extends testBase {

    @Test(description = "Reserve a number from SIMTest for testing")
    public void accessReservations() {
        // Initialize page objects
        simtestHomePage homePage = new simtestHomePage(driver);
        simtestSelectCountryPage countryPage = new simtestSelectCountryPage(driver);
        simTestReserveNumbersPage numbersPage = new simTestReserveNumbersPage(driver);
        simTestReserveFreeSlotPage slotPage = new simTestReserveFreeSlotPage(driver);
        simTestActiveNumbersPage activePage = new simTestActiveNumbersPage(driver);

        // Execute reservation flow
        homePage.openReservations();
        countryPage.selectCountry("United Arab Emirates");
        numbersPage.chooseNumberByIndex(1);
        slotPage.clickFindFreeSlots();
        slotPage.chooseFreeSlotByIndex(2);
        
        // Navigate to active numbers
        homePage.openTestingTab();
        homePage.openSMS();
        
        // Verify number in active list
        String reservedNumber = TestContext.getData().getReservedNumberText();
        activePage.verifyReservedNumberInActiveList(reservedNumber);

        /*
         * Available countries with numbers for automation:
         * - Bahrain
         * - United Arab Emirates
         * - Turkey
         * - Spain
         * - Senegal
         * - Portugal
         * - Oman
         */
    }
}


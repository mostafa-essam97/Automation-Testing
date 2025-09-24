//package TestCases;
//
//import TestUtilities.DriverManager;
//import org.openqa.selenium.WebDriver;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//public class FullFlowTest {
//    private int index;
//    private WebDriver driver;
//
//    public FullFlowTest(int index) {
//        this.index = index;
//    }
//
//    @BeforeClass
//    public void setUp() {
//        // افتح البراوزر قبل أي تست
//        driver = DriverManager.getDriver("chrome");
//    }
//
//    @Test
//    public void runFullFlow() {
//        try {
//            // 1. Login
//            new TC_01_SIMTESTLogin(driver).loginToSIMTestWebsite();
//
//            // 2. Reserve Number with index
//            new TC_02_SIMTestReservation(driver, index).accessReservations();
//
//            // 3. Open Shofha and Subscribe
//            new TC_03_OpenShofhaAndSubscribe(driver).openShofha();
//
//            // 4. Get OTP Code from Simtest
//            new TC_04_SimTestGetOTPCode(driver).getOTPfromSimtest();
//
//            // 5. Verify OTP in Portal
//            new TC_05_VerifyPortalOTP(driver).PortalOTPCode();
//
//            // 6. Cancel Subscription
//            new TC_06_CancelUserSubscription(driver).userCancelSubscription();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e; // خلّي TestNG يسجّل التست كـ failed
//        }
//    }
//
//    @AfterClass(alwaysRun = true)
//    public void tearDown() {
//        if (driver != null) {
//            DriverManager.quitDriver();
//        }
//    }
//}

package TestCases;

import TestUtilities.DriverManager;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class testBase {
    protected static WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    @Parameters({"browser"})
    public void startBrowser(@Optional("chrome") String browserName) {
        if (driver == null) {
            driver = DriverManager.getDriver(browserName);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.get("https://app.simtest.it/login");
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
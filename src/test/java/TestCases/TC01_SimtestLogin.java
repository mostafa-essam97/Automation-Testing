package TestCases;

import Pages.*;
import Utilities.TestContext;
import config.ConfigReader;
import org.testng.annotations.Test;

import static TestCases.testBase.driver;

/**
 * TC01_SimtestLogin - Test case for SIMTest login functionality
 */
public class TC01_SimtestLogin extends testBase {

    @Test(description = "Login to SIMTest website with valid credentials")
    public void loginToSIMTestWebsite() {
        simtestLoginPage loginPage = new simtestLoginPage(driver);
        
        // Get credentials from config (with env var override support)
        String username = ConfigReader.getSimtestUsername();
        String password = ConfigReader.getSimtestPassword();
        
        // Fallback for testing (should be set via environment variables in production)
        if (username == null || username.isEmpty()) {
            username = "arpuplus";  // Default for local testing only
        }
        if (password == null || password.isEmpty()) {
            password = "domain.arpu@";  // Default for local testing only
        }
        
        // Store in test context for reporting
        TestContext.TestData data = TestContext.getData();
        data.setSimtestUsername(username);
        data.setSimtestPassword(password);
        
        // Perform login
        loginPage.login(username, password);
    }
}


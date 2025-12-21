package TestCases;

import Pages.simtestLoginPage;
import Utilities.TestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class TC_01_SIMTESTLogin extends testBase {
    private static final Logger logger = LoggerFactory.getLogger(TC_01_SIMTESTLogin.class);

    @Test
    public void loginToSIMTestWebsite() {
        logger.info("========================================");
        logger.info("Starting TC_01: SIMTest Login");
        logger.info("========================================");

        simtestLoginPage loginPage = new simtestLoginPage(driver);

        String username = "arpuplus";
        String password = "domain.arpu@";

        // Store credentials in TestContext for reference
        TestContext.TestData data = TestContext.getData();
        data.setSimtestUsername(username);
        data.setSimtestPassword(password);

        logger.info("Step 1: Logging in to SIMTest website...");
        logger.info("   Username: {}", username);

        loginPage.login(username, password);

        logger.info("✅ TC_01 completed successfully!");
        logger.info("   Logged in as: {}", username);
    }
}
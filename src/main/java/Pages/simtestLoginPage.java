package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

/**
 * SimtestLoginPage - Page Object for SIMTest login functionality
 */
public class simtestLoginPage extends BasePage {

    public simtestLoginPage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements ============
    
    @FindBy(id = "username")
    private WebElement usernameField;

    @FindBy(id = "password")
    private WebElement passwordField;

    @FindBy(xpath = "//input[@type='submit' and @value='Sign In']")
    private WebElement signInBtn;

    // ============ Actions ============

    /**
     * Login to SIMTest with provided credentials
     */
    public void login(String username, String password) {
        try {
            logger.info("Attempting to login with username: {}", username);
            
            sendKeys(usernameField, username);
            logger.info("Username entered successfully");
            
            sendKeys(passwordField, password);
            logger.info("Password entered successfully");
            
            click(signInBtn);
            logger.info("Login button clicked");
            
            // Wait for page to load after login
            waitForPageStability();
            
            logger.info("Login completed successfully");
        } catch (Exception e) {
            logger.error("Login failed: {}", e.getMessage());
            Assert.fail("Login failed: " + e.getMessage());
        }
    }

    /**
     * Check if login page is displayed
     */
    public boolean isLoginPageDisplayed() {
        return isElementDisplayed(usernameField) && isElementDisplayed(passwordField);
    }
}

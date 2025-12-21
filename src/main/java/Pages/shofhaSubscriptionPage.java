package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

/**
 * ShofhaSubscriptionPage - Page Object for Shofha NEW subscription landing page
 * URL: https://subscription.shofha.com/subscriptionLandingPage/Web/DCB
 */
public class shofhaSubscriptionPage extends BasePage {

    public shofhaSubscriptionPage(WebDriver driver) {
        super(driver);
    }

    // ============ Elements (Updated for new page) ============

    // Country dropdown - id="countries"
    @FindBy(id = "countries")
    private WebElement countriesDropdown;

    // Phone number field - id="msisdn"
    @FindBy(id = "msisdn")
    private WebElement phoneField;

    // Next/Submit button - id="send_btn"
    @FindBy(id = "send_btn")
    private WebElement nextBtn;

    // Error message
    @FindBy(id = "msisdnError")
    private WebElement errorMessage;

    // ============ Actions ============

    /**
     * Select country by country code from dropdown
     * Available codes: 20 (Egypt), 971 (UAE), 966 (Saudi), 973 (Bahrain), 
     * 965 (Kuwait), 974 (Qatar), 216 (Tunisia), 964 (Iraq), 
     * 212 (Morocco), 968 (Oman), 249 (Sudan), 221 (Senegal)
     */
    public void selectCountryByCode(String countryCode) {
        try {
            logger.info("Selecting country code: {}", countryCode);
            
            // Wait for dropdown to be clickable
            waitForClickable(countriesDropdown);
            
            Select select = new Select(countriesDropdown);
            select.selectByValue(countryCode);
            
            // Wait for any UI updates
            waitForPageStability();
            
            // Log the selected option text
            String selectedText = select.getFirstSelectedOption().getText();
            logger.info("✅ Country selected successfully: {} ({})", selectedText, countryCode);
            
        } catch (Exception e) {
            logger.error("❌ Failed to select country code: {}", countryCode);
            logger.error("   Error: {}", e.getMessage());
            logger.error("   Available codes: 20, 971, 966, 973, 965, 974, 216, 964, 212, 968, 249, 221");
            Assert.fail("Failed to choose country code: " + countryCode + " - " + e.getMessage());
        }
    }

    /**
     * Enter the phone number (WITHOUT country code)
     * The country code is selected separately in the dropdown
     * 
     * @param phoneNumber The phone number without country code (e.g., "32014805" not "97332014805")
     */
    public void enterPhoneNumber(String phoneNumber) {
        try {
            logger.info("Entering phone number: {}", phoneNumber);
            
            // Clear any existing value first
            WebElement field = waitForClickable(phoneField);
            field.clear();
            field.sendKeys(phoneNumber);
            
            logger.info("✅ Phone number entered successfully");
            
        } catch (Exception e) {
            logger.error("❌ Failed to enter phone number: {}", phoneNumber);
            logger.error("   Error: {}", e.getMessage());
            Assert.fail("Failed to enter phone number: " + phoneNumber);
        }
    }

    /**
     * Enter the reserved number - extracts phone number from full number
     * If fullNumber = "97332014805" and countryCode = "973", it will enter "32014805"
     * 
     * @param fullNumber The full number including country code
     * @param countryCode The country code to remove from the beginning
     */
    public void enterReservedNumber(String fullNumber, String countryCode) {
        try {
            logger.info("Processing full number: {} with country code: {}", fullNumber, countryCode);
            
            String phoneNumber;
            if (fullNumber.startsWith(countryCode)) {
                phoneNumber = fullNumber.substring(countryCode.length());
                logger.info("Extracted phone number: {}", phoneNumber);
            } else {
                // If doesn't start with country code, use as is
                phoneNumber = fullNumber;
                logger.warn("Number doesn't start with country code, using as is: {}", phoneNumber);
            }
            
            enterPhoneNumber(phoneNumber);
            
        } catch (Exception e) {
            logger.error("❌ Failed to process reserved number");
            logger.error("   Full number: {}", fullNumber);
            logger.error("   Country code: {}", countryCode);
            logger.error("   Error: {}", e.getMessage());
            Assert.fail("Failed to enter reserved number: " + e.getMessage());
        }
    }

    /**
     * Click the Next button to proceed
     */
    public void clickNextButton() {
        try {
            logger.info("Clicking 'التالي' (Next) button...");
            
            // Scroll to button if needed
            scrollToElement(nextBtn);
            
            click(nextBtn);
            
            // Wait for navigation
            waitForPageStability();
            
            String currentUrl = driver.getCurrentUrl();
            logger.info("✅ Navigated to: {}", currentUrl);
            
        } catch (Exception e) {
            logger.error("❌ Failed to click Next button: {}", e.getMessage());
            Assert.fail("Failed to click Next button: " + e.getMessage());
        }
    }

    /**
     * Check if there's an error message displayed
     */
    public boolean hasError() {
        try {
            return isElementDisplayed(errorMessage) && 
                   errorMessage.getText().contains("رجاء التاكد");
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Get error message text
     */
    public String getErrorMessage() {
        try {
            if (isElementDisplayed(errorMessage)) {
                return errorMessage.getText();
            }
        } catch (Exception e) {
            logger.debug("Error message element not found");
        }
        return "";
    }

    /**
     * Complete subscription entry flow
     * 1. Select country
     * 2. Enter phone number
     * 3. Click next
     * 
     * @param fullNumber The full phone number with country code (e.g., "97332014805")
     * @param countryCode The country code (e.g., "973")
     */
    public void completeSubscriptionEntry(String fullNumber, String countryCode) {
        logger.info("========================================");
        logger.info("Starting subscription entry flow");
        logger.info("   Full number: {}", fullNumber);
        logger.info("   Country code: {}", countryCode);
        logger.info("========================================");
        
        // Step 1: Select country
        selectCountryByCode(countryCode);
        
        // Step 2: Enter phone number (without country code)
        enterReservedNumber(fullNumber, countryCode);
        
        // Step 3: Click next
        clickNextButton();
        
        // Check for errors
        if (hasError()) {
            String error = getErrorMessage();
            logger.error("❌ Error after submission: {}", error);
            Assert.fail("Subscription entry failed: " + error);
        }
        
        logger.info("✅ Subscription entry completed successfully");
    }
}

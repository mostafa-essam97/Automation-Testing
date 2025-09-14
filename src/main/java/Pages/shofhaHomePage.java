package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class shofhaHomePage extends pageBase{
    public shofhaHomePage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    //Define Elements
    @FindBy(id = "Sub_nav")
    WebElement subscriptionBtn;

    @FindBy(css = "img[alt='user']")
    WebElement profileIcon;

    @FindBy(xpath = "/html/body/app-root/app-navbar/header/nav/div/div/div[3]/div/a[1]")
    WebElement accountSettingsBtn;


    @FindBy(id = "elmloader")
    WebElement loader;

    //Define Functions
    public void openSubscriptionPage(){
        try {
            wait.until(ExpectedConditions.visibilityOf(subscriptionBtn)).click();
            Thread.sleep(3000);
            System.out.println("Subscription page opened successfully");
            checkCurrentURL("https://shofha.com/payment/dcb/subscribe");
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public void openUserProfileIcon(){
        try {
            wait.until(ExpectedConditions.visibilityOf(profileIcon)).click();
            //Assert.assertEquals(accountSettingsBtn.getText(), "إعدادت الحساب");
            System.out.println("Profile icon opened successfully.");
        } catch (Exception e) {
            System.out.println("Something went wrong, Can't open the profile icon. "+ e.getMessage());
            Assert.fail("Can't open the profile icon. " + e.getMessage());
        }
    }

    public void openUserAccountSetting(){
        try {
            wait.until(ExpectedConditions.invisibilityOf(loader));
            Thread.sleep(2000);
            wait.until(ExpectedConditions.elementToBeClickable(accountSettingsBtn)).click();
            Thread.sleep(2000);
            checkCurrentURL("https://shofha.com/accountSettings");
            System.out.println(accountSettingsBtn.getText()+"\nAccount setting page opened successfully.");
        } catch (Exception e) {
            System.out.println("Something went wrong, Can't open the user account setting. "+ e.getMessage());
            Assert.fail("Can't open the user account setting. " + e.getMessage());
        }
    }
}
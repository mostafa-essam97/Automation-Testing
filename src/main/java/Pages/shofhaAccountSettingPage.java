package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class shofhaAccountSettingPage extends pageBase{
    Actions actions;

    public shofhaAccountSettingPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        actions = new Actions(driver);
    }

    //Define Elements
    @FindBy(css = "button[data-target='#collapseThree']")
    WebElement subscriptionDetailsBtn;

    @FindBy (xpath = "//button[@data-target='#cancelBilling']")
    WebElement cancelSubBtn;

    @FindBy (className = "confirmation")
    WebElement cancellationPopupScreen;

    @FindBy (xpath = "//p[contains(text(),'هل انت متأكد من الغاء الاشتراك؟')]")
    WebElement popupCancelScreenTitle;


    @FindBy (className = "confirmYes")
    WebElement yesBtn;


    //Define Functions
    public void accessSubscriptionDetails(){
        try {
            scrollToElement(subscriptionDetailsBtn);
            wait.until(ExpectedConditions.elementToBeClickable(subscriptionDetailsBtn)).click();
            //Assert.assertEquals(cancelSubBtn.getText(),"إلغاء الاشتراك");
            System.out.println("User subscription details section opened successfully.");
        } catch (Exception e) {
            System.out.println("Can't open the user subscription details. ");
            Assert.fail("Something went wrong, Can't open the user subscription details. " + e.getMessage());
        }
    }

    public void CancelSubscription(){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(cancelSubBtn)).click();
            Assert.assertEquals(popupCancelScreenTitle.getText(),"هل انت متأكد من الغاء الاشتراك؟");
            System.out.println("Cancellation popup screen displayed clearly.");
            Thread.sleep(1000);
        }
        catch (Exception e){
            System.out.println("Can't open the cancellation category. "+e.getMessage());
            Assert.fail("Can't open the cancellation popup screen" + e.getMessage());
        }
    }

    public void confirmCancellation(){
        try{
            actions.moveToElement(yesBtn).perform();
            wait.until(ExpectedConditions.elementToBeClickable(yesBtn)).click();
            Thread.sleep(5000);
            System.out.println("Confirm cancellation successfully.");
//            wait.until(ExpectedConditions.invisibilityOf(cancellationPopupScreen));
//            System.out.println("Confirmation popup screen disappeared successfully. " + cancellationPopupScreen.getText());
        }
        catch (Exception e){
            System.out.println("Can't find the yes button. " + e.getMessage());
            Assert.fail("Can't click on the yes button." + e.getMessage());
        }
    }
}
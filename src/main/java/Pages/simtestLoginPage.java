package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class simtestLoginPage extends pageBase{
    public simtestLoginPage(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    //Define Element
    @FindBy(id = "username")
    WebElement usernameField;

    @FindBy(id = "password")
    WebElement passwordField;

    @FindBy(xpath = "//input[@type='submit' and @value='Sign In']")
    WebElement signInBtn;

    //Define Function
    public void simTestlogin(String simTestUsername, String simTestPassword){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(usernameField))
                    .sendKeys(simTestUsername);
            System.out.println("Username inserted successfully ==> " + simTestUsername);
            wait.until(ExpectedConditions.elementToBeClickable(passwordField))
                    .sendKeys(simTestPassword);
            System.out.println("Password inserted successfully ==> " + simTestPassword);
            wait.until(ExpectedConditions.elementToBeClickable(signInBtn))
                    .click();
            System.out.println("Login successfully.");
        }
        catch (Exception e){
            System.out.println("Something want wrong, Please retry. " + e.getMessage());
        }
    }
}

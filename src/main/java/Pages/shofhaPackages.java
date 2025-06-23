package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class shofhaPackages extends pageBase {
    public String subscriptionTimestamp = "";

    public shofhaPackages(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //Define Elements
    @FindBy(css = "div.PKG .col-md-4")
    List<WebElement> packages;

    //Define Functions
    public String getSubscriptionTimestamp(){
        return subscriptionTimestamp;
    }

    public void selectPackage(int index) {
        try {
            wait.until(ExpectedConditions.visibilityOfAllElements(packages));

            if (index < 0 || index > packages.size()) {
                System.out.println("Invalid index, The available packages are: " + "' " + packages.size() + " '");
                Assert.fail("❌ Invalid index: ' " + index + " '. Available packages: " + "' " + packages.size() + " '");
            }
            else{
                System.out.println("You are going to subscribe with the " + getOrdinal(index + 1) + " package.");
                WebElement selectedPackage = packages.get(index);
                String packageType = selectedPackage.findElement(By.tagName("span")).getText().trim(); // To get the package type [Daily, Weekly, Monthly, ... etc]
                String packagePrice = selectedPackage.findElement(By.className("CustomPrice")).getText().trim(); // To get the selected package's price
                WebElement subNowBtn = selectedPackage.findElement(By.tagName("button"));
                wait.until(ExpectedConditions.visibilityOf(subNowBtn));
                Thread.sleep(2000);
                subNowBtn.click();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                subscriptionTimestamp = LocalDateTime.now().format(formatter);

                System.out.println("You have chosen the " + packageType + " with price " + packagePrice + ".\n" +
                        "Please insert a valid OTP to enjoy with SHOFHA.\n" +
                        "⏱️ Subscription time recorded: " + subscriptionTimestamp);
            }
        } catch (Exception e) {
            System.out.println("Failed to select package." + e.getMessage());
        }
    }
}
package Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class pageBase {
    protected WebDriver driver;
    WebDriverWait wait;

    public pageBase(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    public static String getOrdinal(int number) {
        if (number <= 0) return String.valueOf(number);

        if (number % 100 >= 11 && number % 100 <= 13) {
            return number + "th";
        }

        switch (number % 10) {
            case 1: return number + "st";
            case 2: return number + "nd";
            case 3: return number + "rd";
            default: return number + "th";
        }
    }

    public boolean checkCurrentURL(String expectedURL) {
        String currentURL = driver.getCurrentUrl();
        if (!currentURL.equals(expectedURL)) {
            System.out.println("URLs aren't match.");
            System.out.println("Expected: " + expectedURL);
            System.out.println("Current: " + currentURL);
            return false;
        }
        System.out.println("URLs are match.");
        System.out.println("Expected: " + expectedURL);
        System.out.println("Current: " + currentURL);
        return true;
    }
}

package Pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class accessShofhaPortal extends pageBase {
    public accessShofhaPortal(WebDriver driver) {
        super(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }
    //Define Elements

    //Define Functions
    public void openShofhaInNewTap() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.open()");

            // خزن كل التابات المفتوحة
            List<String> openedTaps = new ArrayList<>(driver.getWindowHandles());

            // Open last tap
            driver.switchTo().window(openedTaps.get(openedTaps.size() - 1));

            //Open shofha portal
            driver.get("https://shofha.com/");
            System.out.println("✅ Shofha opened in new tab successfully.");
        } catch (Exception e) {
            System.out.println("❌ Failed to open Shoofha in new tab: " + e.getMessage());
            Assert.fail("Can't open Shofha website in new tab.");
        }
    }

    public void switchBackToSimtestTab() {
        try {
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());

            // ارجع لأول تابة (اللي فيها SIMTest)
            driver.switchTo().window(tabs.get(0));
            System.out.println("🔁 Switched back to SIMTest tab.");
            Thread.sleep(3000);

        } catch (Exception e) {
            System.out.println("❌ Failed to switch back to SIMTest tab: " + e.getMessage());
            Assert.fail("Can't switch back to SIMTest tab.");
        }
    }

    public void switchFromSimtestTabToShofhaTap() {
        try {
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());

            // ارجع لأول تابة (اللي فيها Shofha)
            driver.switchTo().window(tabs.get(1));
            System.out.println("🔁 Switched back to Shofha tab.");
            Thread.sleep(3000);
        } catch (Exception e) {
            System.out.println("❌ Failed to switch back to Shofha tab: " + e.getMessage());
            Assert.fail("Can't switch back to Shofha tab.");
        }
    }
}
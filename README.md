# Shofha Test Automation Framework

A Selenium-based test automation framework for testing the Shofha streaming service subscription flow using SIMTest virtual numbers.

## 🏗️ Architecture

```
src/
├── main/java/
│   ├── config/
│   │   └── ConfigReader.java          # Configuration management
│   ├── Pages/                          # Page Object Model classes
│   │   ├── BasePage.java              # Base class for all pages
│   │   ├── SimtestLoginPage.java      # SIMTest login
│   │   ├── SimtestHomePage.java       # SIMTest dashboard
│   │   ├── SimtestSelectCountryPage.java
│   │   ├── SimtestReserveNumbersPage.java
│   │   ├── SimtestReserveFreeSlotPage.java
│   │   ├── SimtestActiveNumbersPage.java
│   │   ├── SimtestMessagesPage.java   # SMS/OTP extraction
│   │   ├── ShofhaHomePage.java        # Shofha portal
│   │   ├── ShofhaSubscriptionPage.java
│   │   ├── ShofhaLandingPage.java
│   │   ├── ShofhaPackagesPage.java
│   │   ├── ShofhaAccountSettingsPage.java
│   │   ├── VerifySubscriptionPage.java
│   │   └── AccessShofhaPortal.java    # Tab management
│   └── Utilities/
│       ├── TestContext.java           # Thread-safe test data
│       └── EmailReportSender.java     # Email notifications
│
└── test/
    ├── java/
    │   ├── TestCases/
    │   │   ├── TestBase.java          # Base test class
    │   │   ├── TC01_SimtestLogin.java
    │   │   ├── TC02_SimtestReservation.java
    │   │   ├── TC03_OpenShofhaAndSubscribe.java
    │   │   ├── TC04_SimtestGetOTPCode.java
    │   │   ├── TC05_VerifyPortalOTP.java
    │   │   └── TC06_CancelUserSubscription.java
    │   └── TestUtilities/
    │       ├── DriverManager.java      # WebDriver management
    │       ├── ExtentManager.java      # Report management
    │       ├── ExtentTestManager.java
    │       ├── ScreenshotUtil.java
    │       ├── TestListener.java       # TestNG listener
    │       ├── RetryAnalyzer.java      # Auto-retry failed tests
    │       └── RetryTransformer.java
    └── resources/
        ├── config.properties           # Configuration file
        └── testng.xml                  # Test suite definition
```

## ✨ Features

- **Page Object Model (POM)** - Clean separation of test logic and page interactions
- **Thread-safe Test Data** - Supports parallel test execution using ThreadLocal
- **Configuration Management** - Externalized config with environment variable support
- **Automatic Retry** - Flaky test handling with configurable retry count
- **Extent Reports** - Beautiful HTML test reports with screenshots
- **Email Notifications** - Automatic report delivery via email
- **SLF4J Logging** - Consistent logging throughout the framework
- **Explicit Waits** - No Thread.sleep, proper wait strategies

## 🚀 Getting Started

### Prerequisites

- Java 15+
- Maven 3.6+
- Chrome/Firefox/Edge browser

### Installation

```bash
# Clone the repository
git clone <repository-url>
cd SIMTestAccessNumbers

# Install dependencies
mvn clean install -DskipTests
```

### Configuration

1. **Set environment variables** (recommended for security):

```bash
# Windows PowerShell
$env:SIMTEST_USERNAME = "your-username"
$env:SIMTEST_PASSWORD = "your-password"
$env:EMAIL_FROM = "alerts@example.com"
$env:EMAIL_PASSWORD = "your-email-password"

# Linux/Mac
export SIMTEST_USERNAME="your-username"
export SIMTEST_PASSWORD="your-password"
export EMAIL_FROM="alerts@example.com"
export EMAIL_PASSWORD="your-email-password"
```

2. **Or edit `src/test/resources/config.properties`**:

```properties
browser=chrome
headless=false
default.timeout=10
simtest.username=your-username
simtest.password=your-password
```

### Running Tests

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn test -Dtest=TC01_SimtestLogin

# Run with specific browser
mvn test -Dbrowser=firefox

# Run in headless mode
mvn test -Dheadless=true
```

## 📊 Test Reports

After test execution, reports are available at:
- **Extent Report**: `test-output/ExtentReport.html`
- **TestNG Report**: `target/surefire-reports/index.html`
- **Screenshots**: `test-output/screenshots/`

## 🔄 Test Flow

1. **TC01** - Login to SIMTest platform
2. **TC02** - Reserve a virtual number from selected country
3. **TC03** - Open Shofha and start subscription with reserved number
4. **TC04** - Extract OTP from SIMTest SMS messages
5. **TC05** - Verify OTP on Shofha portal
6. **TC06** - Cancel user subscription

## 📁 Key Classes

### BasePage
Base class for all Page Objects providing:
- WebDriverWait instances (short, default, long)
- Common actions (click, sendKeys, getText)
- Scroll and wait utilities
- SLF4J logger

### TestContext
Thread-safe test data storage using ThreadLocal:
```java
TestContext.getData().setOtpCode("1234");
String otp = TestContext.getData().getOtpCode();
```

### ConfigReader
Configuration with environment variable support:
```java
String username = ConfigReader.getSimtestUsername();
int timeout = ConfigReader.getDefaultTimeout();
```

## 🔧 Customization

### Adding New Tests

1. Create a new test class extending `TestBase`
2. Use page objects for interactions
3. Store data in `TestContext`

```java
public class TC07_NewTest extends TestBase {
    @Test
    public void newTestCase() {
        SimtestHomePage home = new SimtestHomePage(driver);
        home.openReservations();
        // ... test logic
    }
}
```

### Adding New Pages

1. Create a new class extending `BasePage`
2. Define elements with `@FindBy`
3. Implement action methods

```java
public class NewPage extends BasePage {
    @FindBy(id = "element-id")
    private WebElement myElement;
    
    public void doSomething() {
        click(myElement);
        logger.info("Did something");
    }
}
```

## 🛡️ Security Notes

- **Never commit credentials** to version control
- Use environment variables for sensitive data
- The `config.properties` file supports `${ENV_VAR}` syntax
- Add sensitive files to `.gitignore`

## 📝 License

This project is proprietary to Arpu Telecommunication Services.

## 👥 Contributors

- QA Team @ Shofha

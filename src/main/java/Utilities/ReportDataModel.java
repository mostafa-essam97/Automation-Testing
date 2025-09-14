package Utilities;

public class ReportDataModel {
    // Execution Data
    private String testCaseName;
    private String className;
    private String status;
    private String duration;

    // Business Data
    private String username;
    private String reservedNumberText;
    private String fullReservedNumber;
    private String countryCode;
    private String packageType;
    private String packagePrice;
    private String subscriptionTimestamp;
    private String otpCode;

    // Getters & Setters
    public String getTestCaseName() { return testCaseName; }
    public void setTestCaseName(String testCaseName) { this.testCaseName = testCaseName; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getReservedNumberText() { return reservedNumberText; }
    public void setReservedNumberText(String reservedNumberText) { this.reservedNumberText = reservedNumberText; }

    public String getFullReservedNumber() { return fullReservedNumber; }
    public void setFullReservedNumber(String fullReservedNumber) { this.fullReservedNumber = fullReservedNumber; }

    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }

    public String getPackageType() { return packageType; }
    public void setPackageType(String packageType) { this.packageType = packageType; }

    public String getPackagePrice() { return packagePrice; }
    public void setPackagePrice(String packagePrice) { this.packagePrice = packagePrice; }

    public String getSubscriptionTimestamp() { return subscriptionTimestamp; }
    public void setSubscriptionTimestamp(String subscriptionTimestamp) { this.subscriptionTimestamp = subscriptionTimestamp; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
}
package Utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * TestContext - Thread-safe test data management using ThreadLocal
 * Supports parallel test execution without data conflicts
 */
public class TestContext {
    private static final Logger logger = LoggerFactory.getLogger(TestContext.class);

    // ThreadLocal holder for test data - each thread gets its own instance
    private static final ThreadLocal<TestData> testDataHolder = ThreadLocal.withInitial(() -> {
        logger.debug("Creating new TestData instance for thread: {}", Thread.currentThread().getName());
        return new TestData();
    });

    /**
     * Get the TestData for current thread
     */
    public static TestData getData() {
        return testDataHolder.get();
    }

    /**
     * Clear test data for current thread (call in @AfterMethod or @AfterTest)
     */
    public static void clear() {
        logger.debug("Clearing TestData for thread: {}", Thread.currentThread().getName());
        testDataHolder.remove();
    }

    /**
     * Inner class holding all test data
     */
    public static class TestData {

        // Login Info
        private String simtestUsername;
        private String simtestPassword;

        // Reservation Info
        private List<String> allReservedNumbers;
        private String reservedNumberText;
        private String fullReservedNumber;
        private String countryCode;

        // Subscription Info
        private String packageType;
        private String packagePrice;
        private String subscriptionTimestamp;
        private String otpCode;

        // Failure Info
        private String failureReason;

        // ============ Getters & Setters ============

        public String getSimtestUsername() {
            return simtestUsername;
        }

        public void setSimtestUsername(String simtestUsername) {
            this.simtestUsername = simtestUsername;
        }

        public String getSimtestPassword() {
            return simtestPassword;
        }

        public void setSimtestPassword(String simtestPassword) {
            this.simtestPassword = simtestPassword;
        }

        public List<String> getAllReservedNumbers() {
            return allReservedNumbers;
        }

        public void setAllReservedNumbers(List<String> allReservedNumbers) {
            this.allReservedNumbers = allReservedNumbers;
        }

        public String getReservedNumberText() {
            return reservedNumberText;
        }

        public void setReservedNumberText(String reservedNumberText) {
            this.reservedNumberText = reservedNumberText;
        }

        public String getFullReservedNumber() {
            return fullReservedNumber;
        }

        public void setFullReservedNumber(String fullReservedNumber) {
            this.fullReservedNumber = fullReservedNumber;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPackageType() {
            return packageType;
        }

        public void setPackageType(String packageType) {
            this.packageType = packageType;
        }

        public String getPackagePrice() {
            return packagePrice;
        }

        public void setPackagePrice(String packagePrice) {
            this.packagePrice = packagePrice;
        }

        public String getSubscriptionTimestamp() {
            return subscriptionTimestamp;
        }

        public void setSubscriptionTimestamp(String subscriptionTimestamp) {
            this.subscriptionTimestamp = subscriptionTimestamp;
        }

        public String getOtpCode() {
            return otpCode;
        }

        public void setOtpCode(String otpCode) {
            this.otpCode = otpCode;
        }

        public String getFailureReason() {
            return failureReason;
        }

        public void setFailureReason(String failureReason) {
            this.failureReason = failureReason;
        }

        @Override
        public String toString() {
            return "TestData{" +
                    "simtestUsername='" + simtestUsername + '\'' +
                    ", reservedNumberText='" + reservedNumberText + '\'' +
                    ", countryCode='" + countryCode + '\'' +
                    ", packageType='" + packageType + '\'' +
                    ", otpCode='" + otpCode + '\'' +
                    ", failureReason='" + failureReason + '\'' +
                    '}';
        }
    }
}

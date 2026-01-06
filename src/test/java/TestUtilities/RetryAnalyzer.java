package TestUtilities;

import config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer - Automatically retries failed tests
 * Useful for handling flaky tests caused by network issues, timing, etc.
 */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(RetryAnalyzer.class);
    
    private int retryCount = 0;
    private final int maxRetryCount;

    public RetryAnalyzer() {
        this.maxRetryCount = ConfigReader.getRetryMaxCount();
    }

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            logger.warn("🔄 Retrying test '{}' - Attempt {}/{}", 
                    result.getName(), retryCount, maxRetryCount);
            return true;
        }
        return false;
    }

    /**
     * Get the current retry count
     */
    public int getRetryCount() {
        return retryCount;
    }

    /**
     * Get max retry count
     */
    public int getMaxRetryCount() {
        return maxRetryCount;
    }
}







package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader - Centralized configuration management
 * Reads from config.properties and supports environment variable overrides
 */
public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static Properties properties;
    private static final String CONFIG_PATH = "src/test/resources/config.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_PATH)) {
            properties.load(input);
            logger.info("Configuration loaded successfully from: {}", CONFIG_PATH);
        } catch (IOException e) {
            logger.error("Failed to load config file: {}", e.getMessage());
            throw new RuntimeException("Configuration file not found: " + CONFIG_PATH);
        }
    }

    /**
     * Get property value with environment variable override support
     * Format: ${ENV_VAR_NAME} in properties file will be replaced with actual env var
     */
    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value != null && value.startsWith("${") && value.endsWith("}")) {
            String envVar = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envVar);
            if (envValue != null && !envValue.isEmpty()) {
                return envValue;
            }
            logger.warn("Environment variable {} not set, returning null for key: {}", envVar, key);
            return null;
        }
        return value;
    }

    /**
     * Get property with default value
     */
    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get integer property
     */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     * Get integer property with default
     */
    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        return value != null ? Integer.parseInt(value) : defaultValue;
    }

    /**
     * Get boolean property
     */
    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    /**
     * Get boolean property with default
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    // ============ Convenience Methods ============

    public static String getBrowser() {
        return get("browser", "chrome");
    }

    public static boolean isHeadless() {
        return getBoolean("headless", false);
    }

    public static int getDefaultTimeout() {
        return getInt("default.timeout", 10);
    }

    public static int getShortTimeout() {
        return getInt("short.timeout", 5);
    }

    public static int getLongTimeout() {
        return getInt("long.timeout", 30);
    }

    public static String getSimtestBaseUrl() {
        return get("simtest.base.url");
    }

    public static String getSimtestLoginUrl() {
        return get("simtest.login.url");
    }

    public static String getShofhaBaseUrl() {
        return get("shofha.base.url");
    }

    public static String getSimtestUsername() {
        return get("simtest.username");
    }

    public static String getSimtestPassword() {
        return get("simtest.password");
    }

    public static String getEmailFrom() {
        return get("email.from");
    }

    public static String getEmailPassword() {
        return get("email.password");
    }

    public static String getEmailSmtpHost() {
        return get("email.smtp.host");
    }

    public static int getEmailSmtpPort() {
        return getInt("email.smtp.port", 587);
    }

    public static String[] getEmailRecipients() {
        String recipients = get("email.recipients", "");
        return recipients.split(",");
    }

    public static int getRetryMaxCount() {
        return getInt("retry.max.count", 2);
    }
}






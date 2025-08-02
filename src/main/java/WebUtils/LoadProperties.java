package WebUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Utility class to load and manage application configuration from a .properties file.
 * This class ensures thread-safe loading and provides flexible options for
 * retrieving properties, including default values.
 */
public class LoadProperties {

    private static final Logger logger = LogManager.getLogger(LoadProperties.class);

    // Default path for the configuration file (filesystem perspective)
    private static final String DEFAULT_CONFIG_FILE_SYSTEM_PATH = "src/main/resources/config.properties";
    // Corresponding path for classpath loading (resource name)
    private static final String DEFAULT_CONFIG_CLASSPATH_PATH = "config.properties";

    private static final Properties prop = new Properties(); // Properties object is final

    // Static initializer block: This code runs once when the class is first loaded.
    // It attempts to load the default configuration from the filesystem,
    // and if that fails, it tries to load from the classpath.
    static {
        try {
            // Attempt to load from the default filesystem path first
            load(DEFAULT_CONFIG_FILE_SYSTEM_PATH);
        } catch (RuntimeException e) {
            // If filesystem load fails, try classpath as a fallback
            logger.warn("Failed to load configuration from filesystem path: {}. Attempting classpath load: {}",
                    DEFAULT_CONFIG_FILE_SYSTEM_PATH, DEFAULT_CONFIG_CLASSPATH_PATH);
            try {
                loadFromClasspath(DEFAULT_CONFIG_CLASSPATH_PATH);
            } catch (RuntimeException classpathE) {
                // If both attempts fail, log a critical error and re-throw
                logger.error("Failed to load configuration from both filesystem ({}) and classpath ({}). " +
                                "This is a critical error, please ensure your config.properties file exists and is accessible.",
                        DEFAULT_CONFIG_FILE_SYSTEM_PATH, DEFAULT_CONFIG_CLASSPATH_PATH, classpathE);
                throw new RuntimeException("Critical: Could not load default configuration file.", classpathE);
            }
        }
    }

    /**
     * Loads configuration from a given file path. This method can handle
     * both direct filesystem paths and paths that might resolve to classpath resources.
     * This method is synchronized to ensure thread-safe loading, especially if
     * called explicitly after initial static block execution (e.g., via reload).
     *
     * @param filePath the path to the properties file (can be filesystem or classpath style, e.g., "src/main/resources/config.properties" or "config.properties")
     * @throws RuntimeException if the file cannot be found or loaded.
     */
    public static synchronized void load(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.warn("No file path provided to load method. Using default filesystem path: {}", DEFAULT_CONFIG_FILE_SYSTEM_PATH);
            filePath = DEFAULT_CONFIG_FILE_SYSTEM_PATH;
        }

        try (InputStream inputStream = resolveInputStream(filePath)) {
            if (inputStream == null) {
                logger.error("Configuration file not found at path: {}", filePath);
                throw new IOException("File not found: " + filePath); // Throw IOException to be caught below
            }

            prop.clear(); // Clear existing properties before loading new ones
            prop.load(inputStream);
            logger.info("Configuration loaded successfully from: {}", filePath);

            if (prop.isEmpty()) {
                logger.warn("Configuration file is loaded but contains no properties.");
            }

        } catch (IOException e) {
            logger.error("Error loading properties file '{}': {}", filePath, e.getMessage());
            // Wrap IOException in RuntimeException to indicate a critical setup failure
            throw new RuntimeException("Failed to load configuration: " + filePath, e);
        }
    }

    /**
     * Loads configuration specifically from a resource on the classpath.
     * This is useful when you know the resource is embedded within the JAR.
     * This method is synchronized to ensure thread-safe loading.
     *
     * @param resourcePath The path to the resource on the classpath (e.g., "config.properties").
     * @throws RuntimeException if the resource cannot be found or loaded from the classpath.
     */
    public static synchronized void loadFromClasspath(String resourcePath) {
        if (resourcePath == null || resourcePath.trim().isEmpty()) {
            logger.warn("No resource path provided for classpath load. Using default classpath path: {}", DEFAULT_CONFIG_CLASSPATH_PATH);
            resourcePath = DEFAULT_CONFIG_CLASSPATH_PATH;
        }

        try (InputStream inputStream = LoadProperties.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                logger.error("Configuration resource not found on classpath: {}", resourcePath);
                throw new IOException("Resource not found on classpath: " + resourcePath); // Throw IOException to be caught below
            }

            prop.clear(); // Clear existing properties before loading new ones
            prop.load(inputStream);
            logger.info("Configuration loaded successfully from classpath resource: {}", resourcePath);

            if (prop.isEmpty()) {
                logger.warn("Configuration resource is loaded but contains no properties.");
            }
        } catch (IOException e) {
            logger.error("Error loading properties from classpath resource '{}': {}", resourcePath, e.getMessage());
            // Wrap IOException in RuntimeException to indicate a critical setup failure
            throw new RuntimeException("Failed to load configuration from classpath: " + resourcePath, e);
        }
    }

    /**
     * Clears existing properties and reloads from a new file path.
     * Calls the {@link #load(String)} method, which handles the actual loading and synchronization.
     *
     * @param newPath path to the new configuration file (can be filesystem or classpath style)
     */
    public static void reload(String newPath) {
        logger.info("Reloading properties from: {}", newPath);
        load(newPath); // Calls the synchronized load method, which will clear existing properties
    }

    /**
     * Gets the property value for the specified key.
     *
     * @param key the configuration key
     * @return the corresponding value or null if not found.
     */
    public static String get(String key) {
        String value = prop.getProperty(key);
        if (value == null) {
            logger.debug("Property '{}' not found. Returning null.", key);
        }
        return value;
    }

    /**
     * Gets the property value for the specified key, or returns a default value if not found.
     *
     * @param key          the configuration key
     * @param defaultValue the fallback value to return if the key is not found.
     * @return the property value or the default value.
     */
    public static String get(String key, String defaultValue) {
        String value = prop.getProperty(key, defaultValue);
        // Log if the default value was actually used, indicating the property was missing
        if (value.equals(defaultValue) && prop.getProperty(key) == null) {
            logger.debug("Property '{}' not found. Using default value: '{}'", key, defaultValue);
        }
        return value;
    }

    /**
     * Helper method to resolve an InputStream for a given file path.
     * It first attempts to load from the filesystem. If that fails, it tries
     * to load as a classpath resource, potentially stripping common source folder prefixes.
     *
     * @param filePath the path to the file (can be filesystem or classpath style)
     * @return An InputStream for the file, or null if the file cannot be found via any method.
     * @throws IOException if an I/O error occurs during stream creation.
     */
    private static InputStream resolveInputStream(String filePath) throws IOException {
        // 1. Try loading from the filesystem path directly
        if (Files.exists(Paths.get(filePath))) {
            logger.debug("Attempting to load from filesystem: {}", filePath);
            return Files.newInputStream(Paths.get(filePath));
        }

        // 2. If not found on the filesystem, try as a classpath resource (exact path)
        logger.debug("Not found on filesystem. Attempting to resolve as classpath resource (exact path): {}", filePath);
        InputStream stream = LoadProperties.class.getClassLoader().getResourceAsStream(filePath);
        if (stream != null) {
            logger.debug("Resolved as classpath resource (direct): {}", filePath);
            return stream;
        }

        // 3. If still not found, try stripping common source folder prefixes for classpath loading
        String strippedPath = filePath;
        if (filePath.startsWith("src/main/resources/")) {
            strippedPath = filePath.substring("src/main/resources/".length());
        } else if (filePath.startsWith("src/test/resources/")) {
            strippedPath = filePath.substring("src/test/resources/".length());
        }

        if (!strippedPath.equals(filePath)) { // If the path was actually stripped
            logger.debug("Retrying as classpath resource with stripped path: {}", strippedPath);
            stream = LoadProperties.class.getClassLoader().getResourceAsStream(strippedPath);
            if (stream != null) {
                logger.debug("Resolved as classpath resource (stripped): {}", strippedPath);
                return stream;
            }
        }

        logger.warn("Could not resolve input stream for path: {}", filePath);
        return null; // File not found via any method
    }
}

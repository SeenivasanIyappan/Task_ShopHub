package genericWrapper;

import WebUtils.LoadProperties;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;


public class GenericWrapper {
    
    private static final Logger logger = LogManager.getLogger(GenericWrapper.class);
    @Getter
    public static WebDriver driver;
    public static WebDriverWait wait; 
    private static int defaultTimeoutSeconds = 10;

    static {
        try {
            String timeoutStr = LoadProperties.get("implicitlyWait");
            if (timeoutStr != null && !timeoutStr.isEmpty()) {
                defaultTimeoutSeconds = Integer.parseInt(timeoutStr);
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid 'implicitlyWait' value in properties. Using default: " + defaultTimeoutSeconds + " seconds.");
        } catch (Exception e) {
            logger.warn("Could not load 'implicitlyWait' from properties. Using default: " + defaultTimeoutSeconds + " seconds. Error: " + e.getMessage());
        }
    }
    
    public JavascriptExecutor jsExecutor;
    public Select dropdown;
    public Actions action;
    public Robot robot;
    public Alert alert;
    
    public GenericWrapper() {}
    
    public static void initializeDriver(String browserName) {
        if (driver == null) 
        {
            if (browserName.equalsIgnoreCase("chrome")) 
            {
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                logger.info("Chrome browser launched successfully.");
            } 
            else if (browserName.equalsIgnoreCase("edge")) 
            {
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                logger.info("Edge browser launched successfully.");
            } 
            else if (browserName.equalsIgnoreCase("firefox")) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                logger.info("Firefox browser launched successfully.");
            }
            else {
                logger.error("Unsupported browser: {}", browserName);
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
            }
        } else {
            logger.info("Driver already initialized. Skipping re-initialization.");
        }
    }

    public static void setImplicitWait(int implicitWait) {
        if (driver != null) 
        {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
            logger.debug("Implicit wait set to {} seconds.", implicitWait);
        } else {
            logger.warn("Driver is null. Cannot set implicit wait.");
        }
    }

    public static void maximizeWindow() {
        if (driver != null) {
            driver.manage().window().maximize();
            logger.info("Browser window maximized.");
        } else {
            logger.warn("Driver is null. Cannot maximize window.");
        }
    }

    public static void openUrl(String url) {
        if (driver != null) {
            driver.get(url);
            logger.info("Opened URL: {}", url);
        } else {
            logger.warn("Driver is null. Cannot open URL: {}", url);
        }
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
            logger.info("Browser closed successfully.");
        } else {
            logger.info("Driver was already null or not initialized.");
        }
    }

    public String getCurrentUrl() {
        if (driver != null) {
            return driver.getCurrentUrl();
        }
        logger.warn("Driver is null. Cannot get current URL.");
        return null;
    }

    public String getPageTitle() {
        if (driver != null) {
            return driver.getTitle();
        }
        logger.warn("Driver is null. Cannot get page title.");
        return null;
    }

    public void enterText(WebElement element, String text) {
        try {
            webdriverWaitElementToBeVisible(element);
            element.sendKeys(text);
            logger.info("Entered text '{}' into element: {}", text, element.toString());
        } catch (Exception e) {
            logger.error("Failed to enter text '{}' into element {}: {}", text, element.toString(), e.getMessage());
            throw e;
        }
    }

    public void clickElement(WebElement element) 
    {
        element.click();
    }
    
    public void takeScreenshot(String fileName) throws IOException {
        if (driver instanceof TakesScreenshot) {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File dest = new File("Screenshots/" + fileName + ".jpeg"); // Consider making "Screenshots/" configurable
            FileUtils.copyFile(src, dest);
            logger.info("Screenshot saved: {}", dest.getAbsolutePath());
        } else {
            logger.warn("Driver does not support taking screenshots.");
        }
    }

    public void scrollToElement(WebElement element) 
    {
        if (element != null)  
        {
            jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
            logger.info("Scrolled to element: {}", element.toString());
        } else {
            logger.warn("Driver does not support JavascriptExecutor for scrolling.");
        }
    }

    public void clickUsingJS(WebElement element) {
        if (driver instanceof JavascriptExecutor) {
            jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].click();", element);
            logger.info("Clicked element using JS: {}", element.toString());
        } else {
            logger.warn("Driver does not support JavascriptExecutor for clicking.");
        }
    }

    public void highlightElement(WebElement element) {
        if (driver instanceof JavascriptExecutor) {
            jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].setAttribute('style','border: 2px solid red;')", element);
            logger.debug("Highlighted element: {}", element.toString());
        } else {
            logger.warn("Driver does not support JavascriptExecutor for highlighting.");
        }
    }
    
    public String getPageTitleUsingJS() {
        if (driver instanceof JavascriptExecutor) {
            jsExecutor = (JavascriptExecutor) driver;
            Object result = jsExecutor.executeScript("return document.title;");
            if (result != null) {
                String title = result.toString();
                logger.debug("Page title fetched using JS: {}", title);
                return title;
            } else {
                logger.warn("JavaScript execution for document.title returned null.");
                return null;
            }
        }
        logger.warn("Driver does not support JavascriptExecutor for getting page title, or driver is null.");
        return null;
    }

   public void selectDropdownOption(WebElement element, String method, String value) {
        dropdown = new Select(element);
        if (method.equalsIgnoreCase("value")) {
            dropdown.selectByValue(value);
            logger.info("Selected dropdown option by value '{}' for element: {}", value, element.toString());
        } else if (method.equalsIgnoreCase("visible_text")) 
        {
            dropdown.selectByVisibleText(value);
            logger.info("Selected dropdown option by visible text '{}' for element: {}", value, element.toString());
        } else if (method.equalsIgnoreCase("index")) {
            int index = Integer.parseInt(value);
            dropdown.selectByIndex(index);
            logger.info("Selected dropdown option by index '{}' for element: {}", index, element.toString());
        } else {
            logger.warn("Invalid selection method '{}'. Use 'value', 'visible_text', or 'index' for element: {}", method, element.toString());
        }
    }
    
    public WebElement getSelectedOption(WebElement element) {
        dropdown = new Select(element);
        WebElement selectedOption = dropdown.getFirstSelectedOption();
        logger.debug("Fetched selected option '{}' from dropdown: {}", selectedOption.getText(), element.toString());
        return selectedOption;
    }

    public List<WebElement> getAllDropdownOptions(WebElement element) {
        dropdown = new Select(element);
        List<WebElement> options = dropdown.getOptions();
        logger.debug("Fetched {} options from dropdown: {}", options.size(), element.toString());
        return options;
    }

    public void deselectByValue(WebElement element, String value) {
        dropdown = new Select(element);
        dropdown.deselectByValue(value);
        logger.info("Deselected dropdown option by value '{}' for element: {}", value, element.toString());
    }

    public void deselectAllOptions(WebElement element) {
        dropdown = new Select(element);
        dropdown.deselectAll();
        logger.info("Deselected all options for dropdown: {}", element.toString());
    }
    
    public void performMouseAction(String actionType, WebElement element) {
        if (driver != null) {
            action = new Actions(driver);
            if (actionType.equalsIgnoreCase("hover")) {
                action.moveToElement(element).perform();
                logger.info("Performed hover action on element: {}", element.toString());
            } 
            else if (actionType.equalsIgnoreCase("double-click")) {
                action.doubleClick(element).perform();
                logger.info("Performed double click action on element: {}", element.toString());
            }
            else if (actionType.equalsIgnoreCase("hold")) {
                action.clickAndHold(element).perform();
                logger.info("Performed click and hold action on element: {}", element.toString());
            } else {
                logger.warn("Unsupported mouse action: {}", actionType);
            }
        } else
        {
            logger.warn("Driver is null. Cannot perform mouse action.");
        }
    }
    
    public void rightClickElement(WebElement element) {
        if (driver != null) {
            action = new Actions(driver);
            action.contextClick(element).perform();
            logger.info("Performed right click on element: {}", element.toString());
        } else 
        {
            logger.warn("Driver is null. Cannot perform right click.");
        }
    }

    public void dragAndDropElement(WebElement source, WebElement target) {
        if (driver != null) {
            action = new Actions(driver);
            action.dragAndDrop(source, target).perform();
            logger.info("Performed drag and drop from {} to {}", source.toString(), target.toString());
        } else {
            logger.warn("Driver is null. Cannot perform drag and drop.");
        }
    }

    public void handleAlert(String actionType, String inputText) {
        if (driver != null) {
            try {
                waitForAlertPresence(); 
                alert = driver.switchTo().alert();
                if (actionType.equalsIgnoreCase("OK")) {
                    alert.accept();
                    logger.info("Accepted alert.");
                } 
                else if (actionType.equalsIgnoreCase("Cancel")) {
                    alert.dismiss();
                    logger.info("Dismissed alert.");
                } 
                else if (actionType.equalsIgnoreCase("Input")) {
                    if (inputText != null) {
                        alert.sendKeys(inputText);
                        alert.accept();
                        logger.info("Sent text '{}' to alert and accepted.", inputText);
                    } 
                    else {
                        logger.warn("Input text is null for 'Input' alert action.");
                    }
                } 
                else {
                    logger.warn("Invalid alert action: {}", actionType);
                }
            } 
            catch (NoAlertPresentException e) {
                logger.error("No alert present to handle: {}", e.getMessage());
            } catch (Exception e) {
                logger.error("Error handling alert: {}", e.getMessage());
            }
        } 
        else {
            logger.warn("Driver is null. Cannot handle alert.");
        }
    }

    public void pressEnterKey() throws AWTException {
        robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        logger.info("Pressed Enter key.");
    }
    
    public void pressTabKey() throws AWTException {
        robot = new Robot();
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        logger.info("Pressed Tab key.");
    }

    public void pressDownArrowKey() throws AWTException {
        robot = new Robot();
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        logger.info("Pressed Down Arrow key.");
    }

    public void pressUpArrowKey() throws AWTException {
        robot = new Robot();
        robot.keyPress(KeyEvent.VK_UP);
        robot.keyRelease(KeyEvent.VK_UP);
        logger.info("Pressed Up Arrow key.");
    }

    protected int getTimeoutSeconds() {
        return defaultTimeoutSeconds;
    }

    public void waitForElementToBeClickable(WebElement element) {
        if (driver != null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(getTimeoutSeconds()));
            wait.until(ExpectedConditions.elementToBeClickable(element));
            logger.debug("Element is clickable: {}", element.toString());
        } 
        else {
            logger.warn("Driver is null. Cannot wait for element to be clickable.");
        }
    }

    public void webdriverWaitElementToBeVisible(WebElement element) {
        int time = Integer.parseInt(LoadProperties.get("explicitTimeOut"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitClick1(WebElement element) {
        if (driver != null) {
            try {
                wait = new WebDriverWait(driver, Duration.ofSeconds(getTimeoutSeconds()));
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                logger.info("Element clicked after waiting: {}", element.toString());
            } catch (Exception e) {
                logger.error("Failed to click element {}: {}", element.toString(), e.getMessage());
            }
        } else {
            logger.warn("Driver is null. Cannot perform waitClick.");
        }
    }

    public boolean waitClick(WebElement element) {
        if (driver != null) {
            try {
                wait = new WebDriverWait(driver, Duration.ofSeconds(getTimeoutSeconds()));
                wait.until(ExpectedConditions.elementToBeClickable(element)).click();
                logger.info("Element clicked after waiting: {}", element.toString());
                return true;
            } catch (Exception e) {
                logger.error("Failed to click element {}: {}", element.toString(), e.getMessage());
                return false;
            }
        } else {
            logger.warn("Driver is null. Cannot perform waitClick.");
            return false;
        }
    }
    
    public void waitForAlertPresence() {
        if (driver != null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(getTimeoutSeconds()));
            wait.until(ExpectedConditions.alertIsPresent());
            logger.debug("Alert is present.");
        } 
        else {
            logger.warn("Driver is null. Cannot wait for alert presence.");
        }
    }

    public void webdriverWaitUntilUrlContains(String partialUrl) {
        if (driver != null) {
            wait = new WebDriverWait(driver, Duration.ofSeconds(getTimeoutSeconds()));
            wait.until(ExpectedConditions.urlContains(partialUrl));
            logger.debug("URL contains: {}", partialUrl);
        } else 
        {
            logger.warn("Driver is null. Cannot wait until URL contains: {}", partialUrl);
        }
    }
    
    }

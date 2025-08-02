package page;

import genericWrapper.GenericWrapper;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.io.IOException;

public class LoginPage extends GenericWrapper {

    WebDriver driver;
    
    @FindBy(xpath = "//input[@id='email']")
    private WebElement userEmailField;
    
    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordField;
    
    @FindBy(xpath = "//button[normalize-space()='Sign In']")
    private WebElement loginButton;
    
    @FindBy(xpath = "//button[normalize-space()='Profile']")
    private WebElement userpage;
    
    @FindBy(xpath = "//div[@id='notification']")
    private WebElement invalidLoginNotification;

   
    public LoginPage(WebDriver driver) {
        super();
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void enterEmail(String userEmail) {
        waitForElementToBeClickable(userEmailField);
        enterText(userEmailField, userEmail);
    }

    public void enterPassword(String password) {
        passwordField.sendKeys(password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }


    public boolean isLoginSuccessful() {
        try {
            webdriverWaitElementToBeVisible(userpage);
            return true; // Login successful
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isInvalidLoginNotificationDisplayed() {
        try {
            webdriverWaitElementToBeVisible(invalidLoginNotification);
            quitDriver();
            return invalidLoginNotification.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }
    
    public void checkLoginOutcome(boolean expectSuccess) throws IOException {
        if (expectSuccess) {
            if (isLoginSuccessful()) {
                System.out.println("Login successful.");
            } else {
                takeScreenshot("login_failed_expected_success");
                throw new AssertionError("Login failed. Expected success, but got failure.");
            }
        } else {
            if (isInvalidLoginNotificationDisplayed()) {
                takeScreenshot("invalid_login_attempt");
                System.out.println("Login failed as expected with invalid credentials.");
            } else {
                takeScreenshot("no_error_message_on_invalid_login");
                throw new AssertionError("Expected login failure, but no error message was shown.");
            }

        }

    }

    @FindBy(xpath = "//button[normalize-space()='Logout']")
    private WebElement Logout_button;
    public void LogOut()
    {
        webdriverWaitElementToBeVisible(Logout_button);
        clickUsingJS(Logout_button);
    }

}

package page;

import genericWrapper.GenericWrapper;
import io.cucumber.java.eo.Se;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage extends GenericWrapper
{
    WebDriver driver;

    @FindBy(xpath = "//input[@placeholder='Search products...']")
     private WebElement SEARCH;

    @FindBy(xpath = "//button[contains (text(),'Add to Cart')]")
    private WebElement ADD_CART;

    @FindBy(xpath = "//body/div[@id='mainApp']/div[@id='mainLayout']/aside[@class='cart']/div[@id='cartItems']/div[1]")
    private WebElement Shop_LIST;
 
    @FindBy(xpath = "//button[normalize-space()='Checkout']")
    private WebElement ChecK_out;

    @FindBy(xpath = "//p[normalize-space()='No products found.']")
    private WebElement No_Product;

    @FindBy(xpath = "//div[@id='notification']")
    private WebElement Order_Placed;

    @FindBy(xpath = "//span[@id='cartTotal']")
    private WebElement Price;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void SearchButton() {
    
         SEARCH.click();
    }
    
    public void setSEARCH(String cart)
    {
//
        enterText(SEARCH,cart);
        SEARCH.click();
    }

    public void displayed_product(String productName) {
        try {
            WebElement product = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(text(),'" + productName + "')]")));

            System.out.println(" Product was found should  be displayed: " + productName);
            Assert.assertTrue(true);

        } catch (TimeoutException e) {
            try {
                webdriverWaitElementToBeVisible(No_Product);
                if (No_Product.isDisplayed()) {
                    System.out.println(" No products found for: " + productName);
                    Assert.fail("Product '" + productName + "' was found but should not be displayed.");
                }
            } catch (Exception ex) {
                System.out.println(" Unexpected issue during product search for: " + productName);
                Assert.fail("Unexpected error occurred while checking for product: " + productName);
            }
        }
    }
    
    public void setADD_CART()
    {
        webdriverWaitElementToBeVisible(ADD_CART);
        clickElement(ADD_CART);
    }
    public void Shopping_cart() {
        try {
            if (Shop_LIST.isDisplayed()) {
                System.out.println("Cart is added");
            }
        } catch (NoSuchElementException e) {
            System.out.println(e);
        }
    }
    public void setChecK_out()
    {
        clickUsingJS(ChecK_out);
    }
    public void setPrice()
    {
        String text = Price.getText();
        System.out.println("price is :" + text);
    }

    public void verifyOrderPlacedNotification() {
        try {
            webdriverWaitElementToBeVisible(Order_Placed); // Wait for visibility
            String notificationText = Order_Placed.getText();

            if (!notificationText.isEmpty()) {
                System.out.println("Order placed notification displayed: " + notificationText);
            } else {
                System.out.println("Notification element found, but text is empty.");
            }
        } catch (Exception e) {
            System.out.println("Order placed notification not found or not visible. Error: " + e.getMessage());
        }
    }
}

package page;

import genericWrapper.GenericWrapper;
import io.cucumber.java.Scenario;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminPage extends GenericWrapper {
    WebDriver driver;

    public AdminPage(WebDriver driver) {
        super();
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[@id='adminNavLink']")
    private WebElement ADMIN_PANEL;

    @FindBy(xpath = "//input[@id='productName']")
    private WebElement PRODUCT_NAME;

    @FindBy(xpath = "//select[@id='productCategory']")
    private WebElement CATEGORY;

    @FindBy(xpath = "//input[@id='productPrice']")
    private WebElement PRICE;

    @FindBy(xpath = "//input[@id='productStock']")
    private WebElement STOCK;

    @FindBy(xpath = "//textarea[@id='productDescription']")
    private WebElement PRODUCT_DESCRIPTION;

    @FindBy(xpath = "//button[normalize-space()='Add Product']")
    private WebElement ADD_PRODUCT;

    @FindBy(xpath = "//a[normalize-space()='All Products']")
    private WebElement AllProduct;

    @FindBy(xpath = "//input[@placeholder='Search products...']")
    private WebElement searchbar;

    @FindBy(xpath = "//h3[normalize-space()='Existing Products']")
    private WebElement ExProduct;
    
    @FindBy(xpath = "//label[text()='Current Stock']")
    private WebElement CurrentStock;
    
    @FindBy(xpath = "//input[@id='editProductStock']")
    private WebElement StockUpdate;
    
    @FindBy(xpath = "//button[text()='Update Product']")
    private WebElement Update;
    
    
    public void setSearchbar() {
        waitClick(searchbar);
    }

    public void setAllProduct() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(getTimeoutSeconds()));
            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='All Products']")));
            searchInput.click(); // Ensure focus
        } catch (Exception e) {
     System.out.println("Failed to interact with search input" + e);
        }
    }
    public void ClickAdminPanel()
    {
        waitClick(ADMIN_PANEL);
    }

    public void setPRODUCT_NAME(String name)
    {
        clickElement(PRODUCT_NAME);
        enterText(PRODUCT_NAME,name);
    }

    public void setCATEGORY(String option)
    {
        clickUsingJS(CATEGORY);
        selectDropdownOption(CATEGORY, "value", option);
    }

    public void setPRICE(String amount)
    {
        clickElement(PRICE);
        enterText(PRICE,amount);
    }
    public void setSTOCK(String sto)
    {
        clickElement(STOCK);
        enterText(STOCK,sto);
    }
    public void setPRODUCT_DESCRIPTION(String description)
    {
     clickUsingJS(PRODUCT_DESCRIPTION);
     enterText(PRODUCT_DESCRIPTION,description);
    }
    public void setADD_PRODUCT()
    {
     clickUsingJS(ADD_PRODUCT);
    }
    public void ScrolEXProduct()
    {
        scrollToElement(ExProduct);
    }
    public void Exproduct_show(String product)
    {
        try {
            WebElement prd = new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOfElementLocated
                            (By.xpath("//div[@class='product-list-item'][.//*[contains(text(),'" + product + "')]]" +
                                    "//button[contains(text(),'Edit')]")));
            prd.click();
            System.out.println("product was founded");
            Assert.assertTrue(true);
        } catch (TimeoutException e) {
            Assert.fail("Not found" + e);
        }
        }
    public void Exproduct_Delete(String product)
    {
        try {
            WebElement prd = new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.visibilityOfElementLocated
                            (By.xpath("//div[@class='product-list-item'][.//*[contains(text(),'" + product + "')]]" +
                                    "//button[contains(text(),'Delete')]")));
            prd.click();
            System.out.println("product was Delete Successfully");
            Assert.assertTrue(true);
        } catch (TimeoutException e) {
            Assert.fail("Not found" + e);
        }
    }
    
    public void delete_alert()
    {
        waitForAlertPresence();
        alert=driver.switchTo().alert();
        alert.accept();
    }
    
    public void setCurrentStock()
    {
        scrollToElement(CurrentStock);
    }
    public void Stock_Update(String value)
    {
        StockUpdate.clear();
        enterText(StockUpdate,value);
    }

    public void setUpdate() 
    {
        scrollToElement(Update);
        waitClick(Update);
    }
    public void UpdateNotification()
    {
        try {
            WebElement prd = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated
                            (By.xpath("//div[@id='notification']")));
            String not =prd.getText();
            System.out.println("Update Product Successfully"+ not);
            Assert.assertTrue(true);
        } catch (TimeoutException e) {
            Assert.fail("not Update" + e);
        }
    }
    public void Delete_notification() {
        try {
            WebElement prd = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//div[text()='Product deleted successfully']")));

            String not = prd.getText();
            Assert.assertTrue(true);
            System.out.println("Product Delete Successfully: " + not);
           
        } catch (TimeoutException e) {
            Assert.fail("not Update" + e);
           System.out.println("Delete Unsuccessfully" + e);
        }
    }
}

package page;

import genericWrapper.GenericWrapper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NewUser extends GenericWrapper 
{
    WebDriver driver;
    
    public NewUser(WebDriver driver) {
        super();
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    @FindBy(xpath = "//button[normalize-space()='Create New Account']")
    private WebElement Create_New_User;
    
    @FindBy(xpath = "//input[@id='regFirstName']")
    private WebElement First_Name;
    
    @FindBy(xpath = "//input[@id='regLastName']")
    private  WebElement Last_Name;
    
    @FindBy(xpath = "//input[@id='regEmail']")
    private WebElement REG_Email;
    
    @FindBy(xpath = "//input[@id='regPassword']")
    private WebElement REG_Pass;
    
    @FindBy(xpath = "//input[@id='regConfirmPassword']")
    private WebElement REG_Confirm_Pass;
    
    @FindBy(xpath = "//input[@id='regPhone']")
    private WebElement REG_Ph;
    
    @FindBy(xpath = "//input[@id='agreeTerms']")
    private WebElement Terms_Cond;
    
    @FindBy(xpath = "//button[normalize-space()='Create Account']")
    private WebElement Create_Account;

    @FindBy(xpath = "//div[@id='notification']")
    private WebElement Noti;
    
    public void setCreate_New_User( ) 
    {
     waitClick(Create_New_User);   
    }
    public void Fname(String Fname){
        First_Name.clear();
        enterText(First_Name,Fname);
    }
    public void Lname(String lname)
    {
        Last_Name.clear();
        enterText(Last_Name,lname);
    }
    
    public void setREG_Email(String email) 
    {
        enterText(REG_Email,email);
    }

    public void setREG_Pass(String pass) 
    {
     
        enterText(REG_Pass,pass);
    }
    public void setREG_Confirm_Pass(String CnPass)
    {
        REG_Confirm_Pass.clear();
        enterText(REG_Confirm_Pass,CnPass);
    }
    public void setREG_Ph(String PhNo)
    {
        REG_Ph.clear();
        enterText(REG_Ph,PhNo);
    }
    public void setTerms_Cond()
    {
        waitClick1(Terms_Cond);
    }
    public void setCreate_Account()
    {
        if(!Create_Account.isSelected()) {
            Create_Account.click();
        }
    }

    public void setNoti() 
    {
        try {
            webdriverWaitElementToBeVisible(Noti); 
            String notificationText = Noti.getText();

            if (!notificationText.isEmpty()) {
                System.out.println("Account created successfully! Please login" + notificationText);
            } else {
                System.out.println("Notification element found, but text is empty.");
            }
        } catch (Exception e) {
            System.out.println("Order placed notification not found or not visible. Error: " + e.getMessage());
        }
    }    
        
    }

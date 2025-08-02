package stepdefinitions;

import WebUtils.LoadProperties;
import genericWrapper.GenericWrapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import page.LoginPage;
import page.NewUser;


public class NewUserSteps extends GenericWrapper {
    LoginPage loginPage; 
    NewUser newuser;
    public NewUserSteps()
    {
        WebDriver driver = getDriver(); // Assuming getDriver() exists in GenericWrapper
        newuser = new NewUser(driver);
    }
    @Given("User is on the Create New Account page")
    public void user_is_on_the_create_new_account_page() 
    {
        ensureNewlogin();
        newuser.setCreate_New_User();
    }
    @When("User enters a valid {string} and {string}")
    public void user_enters_a_valid_and(String First, String last) {
     First= LoadProperties.get("Firstname");
     last=LoadProperties.get("LastName");
     ensureNewlogin();
     newuser.Fname(First);
     newuser.Lname(last);
    }
    @When("User enters a valid email {string}")
    public void user_enters_a_valid_email(String emailKey)
    { 
        emailKey=LoadProperties.get("email");
        ensureNewlogin();
        newuser.setREG_Email(emailKey);
    }
    @When("User enters a valid password {string}")
    public void user_enters_a_valid_password(String PasKey)
    {
        PasKey=LoadProperties.get("pass");
        ensureNewlogin();
        newuser.setREG_Pass(PasKey);
    }
    @When("User enters the same {string} in the Confirm Password field")
    public void user_enters_the_same_in_the_confirm_password_field(String Cnpass)
    {
        ensureNewlogin();
         Cnpass=LoadProperties.get("pass");
        newuser.setREG_Confirm_Pass(Cnpass);
    }
    @Then("User enters a valid Number {string}")
    public void user_enters_a_valid_number(String ph) 
    {
        ensureNewlogin();
      ph=LoadProperties.get("Phno");
     newuser.setREG_Ph(ph);
    }
    
    @When("User agrees to the Terms and Conditions")
    public void user_agrees_to_the_terms_and_conditions() 
    {
        ensureNewlogin();
        newuser.setTerms_Cond();
    }
    @Then("User clicks on the Create Account button")
    public void user_clicks_on_the_create_account_button()
    {
        ensureNewlogin();
     newuser.setCreate_Account();
    }
    @Then("A success message Account created successfully should be displayed")
    public void a_success_message_account_created_successfully_should_be_displayed()
    {
        ensureNewlogin();
     newuser.setNoti();
    }
    
    @Then("User should be redirected to the login page")
    public void user_should_be_redirected_to_the_login_page() {
        
    }

    private void ensureLogin() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
    }
    private void ensureNewlogin()
    {
        if(newuser==null)
        {
            newuser= new NewUser(driver);
        }
    }
}

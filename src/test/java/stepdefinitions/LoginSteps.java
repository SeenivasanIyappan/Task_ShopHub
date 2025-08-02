package stepdefinitions;

import genericWrapper.GenericWrapper;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import page.HomePage;
import page.LoginPage;

import java.io.IOException;
import java.time.Duration;

public class LoginSteps {

    WebDriver driver;
    LoginPage loginPage;
    HomePage homePage;
    boolean isLoginSuccessful = false;
   
    public LoginSteps() {
        this.driver = Hooks.getDriver(); // assuming you have a Hooks class managing the driver
        loginPage = new LoginPage(driver);
    }



    @Given("User is on the login page")
    public void user_is_on_the_login_page() {
        driver = GenericWrapper.getDriver();
        loginPage = new LoginPage(driver);
    }

    @When("User enters valid email {string} and password {string}")
    public void user_enters_valid_email_and_password(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
    }

    @When("User enters invalid email {string} and password {string}")
    public void user_enters_invalid_email_and_password(String email1, String password2) {
        loginPage.enterEmail(email1);
        loginPage.enterPassword(password2);
    }

    @When("clicks the login button")
    public void clicks_the_login_button() {
        loginPage.clickLoginButton();
    }

    @Then("User click the Logout button")
    public void user_click_the_logout_button() {
     loginPage.LogOut();
    }
    
    @Then("User should be redirected to the home page")
    public void user_should_be_redirected_to_the_home_page() throws IOException {
        loginPage.checkLoginOutcome(true);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@placeholder='Search products...']"))
        );
        homePage = new HomePage(driver);  // NOW page is fully loaded

    }
    
    @When("User clicks on the search bar")
    public void user_clicks_on_the_search_bar() {
        ensureHomePageIsInitialized();
        homePage.SearchButton();  // This may fire too early
    }


    @And("User types {string} into the search bar")
    public void user_types_into_the_search_bar(String product) {
        ensureHomePageIsInitialized();
        homePage.setSEARCH(product);
        homePage.SearchButton();
    }

    @And("clicks the search button")
    public void clicks_the_search_button() {
        ensureHomePageIsInitialized();
        homePage.SearchButton();
    }

    @Then("Products related to {string} should be displayed")
    public void products_related_to_should_be_displayed(String product) {
        ensureHomePageIsInitialized();
        homePage.displayed_product(product);
    }

    @Then("Products related to {string} should not be displayed")
    public void products_related_to_should_not_be_displayed(String product1) {
        ensureHomePageIsInitialized();
        homePage.displayed_product(product1);
    }

    @When("User clicks {string} on a displayed product")
    public void user_clicks_add_to_cart_on_a_displayed_product(String productName) {
        ensureHomePageIsInitialized();
        homePage.displayed_product(productName);
    }

    @Then("The product should be added to the shopping cart")
    public void the_product_should_be_added_to_the_shopping_cart() {
        ensureHomePageIsInitialized();
        homePage.Shopping_cart();
    }

    @When("User clicks on the cart icon")
    public void user_clicks_on_the_cart_icon() {
        ensureHomePageIsInitialized();
        homePage.setADD_CART();
    }

    @Then("The cart summary should display the added product with the correct price")
    public void the_cart_summary_should_display_the_added_product_with_the_correct_price() {
        ensureHomePageIsInitialized();
        homePage.setPrice();
    }

    @When("User clicks the Checkout button")
    public void user_clicks_the_checkout_button() {
        ensureHomePageIsInitialized();
        homePage.setChecK_out();
    }

    @Then("User should be redirected to the checkout page")
    public void user_should_be_redirected_to_the_checkout_page() {
        ensureHomePageIsInitialized();
        homePage.verifyOrderPlacedNotification();
    }


    private void ensureHomePageIsInitialized() {
        if (homePage == null) {
            homePage = new HomePage(driver);
        }
    }

}

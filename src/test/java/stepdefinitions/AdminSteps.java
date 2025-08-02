package stepdefinitions;

import genericWrapper.GenericWrapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import page.AdminPage;
import page.HomePage;
import page.LoginPage;

import java.io.IOException;

public class AdminSteps extends GenericWrapper {

    LoginPage loginPage;
    HomePage homePage;
    AdminPage adminPage;
    boolean isLoginSuccessful = false;

    public AdminSteps() {
        adminPage = new AdminPage(driver);
        homePage= new HomePage(driver);
        loginPage=new LoginPage(driver);
    }

    @Given("Admin is on the login page")
    public void admin_is_on_the_login_page() {
        ensureLogin();
    }

    @When("Admin enters valid email {string} and password {string}")
    public void admin_enters_valid_email_and_password(String email, String password) {
        ensureLogin();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
    }

    @When("Admin clicks the login button")
    public void admin_clicks_the_login_button() {
        ensureLogin();
        loginPage.clickLoginButton();
    }

    @Then("Admin should be redirected to the home page")
    public void admin_should_be_redirected_to_the_home_page() throws IOException {
        ensureLogin();
        loginPage.checkLoginOutcome(true);
    }

    @When("Admin clicks on the Admin panel")
    public void admin_clicks_on_the_admin_panel() {
        ensureAdminPageIsInitialized();
        adminPage.ClickAdminPanel();
    }

    @When("Admin adds a new product with name {string}")
    public void admin_adds_a_new_product_with_name(String product) {
        ensureAdminPageIsInitialized();
        adminPage.setPRODUCT_NAME(product);
    }

    @When("Admin selects category {string} and enters price {string}")
    public void admin_selects_category_and_enters_price(String category, String price) {
        ensureAdminPageIsInitialized();
        adminPage.setCATEGORY(category);
        adminPage.setPRICE(price);
    }

    @When("Admin enters initial stock {string} and product description {string}")
    public void admin_enters_initial_stock_and_product_description(String stock, String PD) {
        ensureAdminPageIsInitialized();
        adminPage.setSTOCK(stock);
        adminPage.setPRODUCT_DESCRIPTION(PD);
    }

    @Then("Admin clicks the Add Product button")
    public void admin_clicks_the_add_product_button() {
        ensureAdminPageIsInitialized();
        adminPage.setADD_PRODUCT();
    }

    @Then("Admin click the all Products section")
    public void admin_click_the_all_products_section() {
        ensureAdminPageIsInitialized();
        adminPage.setAllProduct();
    }

    @When("Admin clicks on the search bar")
    public void admin_clicks_on_the_search_bar() {
        ensureAdminPageIsInitialized();
        adminPage.setSearchbar();
    }

    @When("Admin types {string} into the search bar")
    public void admin_types_into_the_search_bar(String product) {
        homePage.setSEARCH(product);
    }

    @Then("Admin clicks on the Existing Products section")
    public void admin_clicks_on_the_existing_products_section() 
    {
        adminPage.ScrolEXProduct();
    }

    @Then("Admin see the {string} list and clicks the edit icon")
    public void admin_see_the_list_and_clicks_the_edit_icon(String Product)
    {
       adminPage.Exproduct_show(Product);
    }

    @Then("Admin enters updated stock value {string}")
    public void admin_enters_updated_stock_value(String stvalue) {
       adminPage.setCurrentStock();
       adminPage.Stock_Update(stvalue);
    }

    @Then("clicks the update button")
    public void clicks_the_update_button() throws IOException {
        adminPage.takeScreenshot("updatestock");
        adminPage.setUpdate();
    }
    @Then("A success notification should be displayed")
    public void a_success_notification_should_be_displayed() {
        adminPage.UpdateNotification();
    }

    @Then("Admin see {string}\" list and clicks the delete icon,and a confirmation popup appears")
    public void admin_see_list_and_clicks_the_delete_icon_and_a_confirmation_popup_appears(String Delete) {
        adminPage.Exproduct_Delete(Delete);
    }
    
    @When("Admin confirms the deletion")
    public void admin_confirms_the_deletion() 
    {
    adminPage.delete_alert();
    }

    @Then("A success notification should be displayed stating the product was deleted")
    public void a_success_notification_should_be_displayed_stating_the_product_was_deleted()  {
          adminPage.Delete_notification();
    }


    private void ensureAdminPageIsInitialized() {
        if (adminPage == null) {
            adminPage = new AdminPage(driver);
        }
    }

    private void ensureLogin() {
        if (loginPage == null) {
            loginPage = new LoginPage(driver);
        }
    }

}

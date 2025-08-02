@PositiveFlow
  
Feature: Successful login to Admin portal and add a new product

  @Regression @AdminFlow
  Scenario Outline: Login, add a new product, search product, add to cart, and checkout
    Given Admin is on the login page
    When Admin enters valid email "<email>" and password "<password>"
    And Admin clicks the login button
    Then Admin should be redirected to the home page
    When Admin clicks on the Admin panel
    And Admin adds a new product with name "<productName>"
    And Admin selects category "<category>" and enters price "<price>"
    And Admin enters initial stock "<stock>" and product description "<description>"
    Then Admin clicks the Add Product button
    Then Admin click the all Products section
    
    When Admin clicks on the search bar
    And Admin types "<productName>" into the search bar
    And clicks the search button
    Then Products related to "<productName>" should be displayed
    When User clicks "Add to Cart" on a displayed product
    Then The product should be added to the shopping cart
    When User clicks on the cart icon
    Then The cart summary should display the added product with the correct price
    When User clicks the Checkout button
    Then User should be redirected to the checkout page
    Then User click the Logout button


    Examples:
      | email             | password | productName     | category    | price | stock | description            |
      | admin@shophub.com | admin123 | Bluetooth Mouse | Electronics | 499   | 20    | Wireless optical mouse |

  @Regression @AdminFlow
  Scenario Outline: Login, Open Admin Panel, and Edit Existing Products
    Given Admin is on the login page
    When Admin enters valid email "<email>" and password "<password>"
    And Admin clicks the login button
    Then Admin should be redirected to the home page
    When Admin clicks on the Admin panel
    Then Admin clicks on the Existing Products section
    And Admin see the "<product>" list and clicks the edit icon
    Then Admin enters updated stock value "<stock>"
    And clicks the update button
    Then A success notification should be displayed
    Then User click the Logout button

    Examples:
      | email             | password | product         | stock |
      | admin@shophub.com | admin123 | Sony WH-1000XM5 | 65    |

  @Regression @AdminFlow
 Scenario Outline: Login, Open Admin Panel, and Delete Existing Products
    Given Admin is on the login page
    When Admin enters valid email "<email>" and password "<password>"
    And Admin clicks the login button
    Then Admin should be redirected to the home page
    When Admin clicks on the Admin panel
    Then Admin clicks on the Existing Products section
    And Admin see "<product>" list and clicks the delete icon,and a confirmation popup appears
    When Admin confirms the deletion
    Then A success notification should be displayed stating the product was deleted
    Then User click the Logout button

    Examples:
      | email             | password | product          |
      | admin@shophub.com | admin123 | Dell Monitor 27" |

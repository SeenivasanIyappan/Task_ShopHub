@PositiveFlow
Feature: Successful Shopping Journey for User and Guest User

  @Regression @LoginFlow
  Scenario Outline: Login, search product, add to cart, and checkout
    Given User is on the login page
    When User enters valid email "<email>" and password "<password>"
    And clicks the login button
    Then User should be redirected to the home page

    When User clicks on the search bar
    And User types "<product>" into the search bar
    And clicks the search button
    Then Products related to "<product>" should be displayed
    When User clicks "Add to Cart" on a displayed product
    Then The product should be added to the shopping cart
    When User clicks on the cart icon
    Then The cart summary should display the added product with the correct price
    When User clicks the Checkout button
    Then User should be redirected to the checkout page
    Then User click the Logout button
    Examples:
      | email            | password | product |
      | user@example.com | user123  | MacBook |
      | guest@demo.com   | guest123 | iPhone  |



  @Regression @NewUser
  Scenario: User successfully creates a new account with valid details
    Given User is on the Create New Account page
    When User enters a valid "First Name" and "last Name"
    And User enters a valid email "Email Address"
    And User enters a valid password "Password"
    And User enters the same "Password" in the Confirm Password field
    Then User enters a valid Number "Phone Number"
    And User agrees to the Terms and Conditions
    And User clicks on the Create Account button
    Then A success message Account created successfully should be displayed
    And User should be redirected to the login page

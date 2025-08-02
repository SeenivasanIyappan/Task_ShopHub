@NegativeFlow
Feature: Unsuccessful Login and Invalid Search
  @Regression @LoginFlow
  Scenario Outline: Unsuccessful login attempt with invalid credentials
    Given User is on the login page
    When User enters invalid email "<email>" and password "<password>"
    And clicks the login button
    Then User should be redirected to the home page

    Examples:
      | email            | password |
      | user@example.com | user12   |

  @Regression @LoginFlow
  Scenario Outline: Valid login but product not found in search
    Given User is on the login page
    When User enters valid email "<email>" and password "<password>"
    And clicks the login button
    Then User should be redirected to the home page
    When User clicks on the search bar
    And User types "<product>" into the search bar
    And clicks the search button
    Then Products related to "<product>" should not be displayed

    Examples:
      | email          | password | product |
      | guest@demo.com | guest123 | vivo    |

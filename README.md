#  ShopHub Automation Project
- This project is an automated test framework for the ShopHub e-commerce platform. It is built using Java, Selenium WebDriver, Cucumber BDD, and Cucumber ExtentReporter, following the Page Object Model (POM) design pattern.

# Features 

- Java + Selenium WebDriver
- Cucumber BDD Framework
- Page Object Model (POM) structure
- Cucumber ExtentReport for test reporting
- Supports multiple test categories: Sanity, Regression, Positive, Admin, and Common
- Executable via Maven or Jenkins

# 🚀 How to Run
- You can run this test suite using Maven commands based on the test profile you want to execute:

- mvn test -Psanity-tests         # @LoginFlow or @NewUser  
- mvn test -Pregression-tests     # @Regression or @NegativeFlow  
- mvn test -Ppositive-tests       # @PositiveFlow  
- mvn test -Padmin-tests          # @AdminFlow  
- mvn test -Pcommon-tests         # All tags together 

###  :file_folder: File Structure
```
.
├── ShopHub
│   ├── Logs
|   |   ├── automation.log  
│   ├── Output
│   │   ├── Screenshots
│   │   │   ├── embedded1.png
│   │   │   └── fail_Unsuccessful_login_attempt_with_invalid_credentials.png
│   │   │   └── fail_Valid_login_but_product_not_found_in_search.png
│   ├── src
│   │   ├── main
│   │   │  ├── genericWrapper
│   │   │  │  ├── GenericWrapper.java
│   │   │  ├── WebUtils
│   │   │  │  ├── LoadProperties.java
│   │   │  ├── resources
│   │   │  │  ├── Features
│   │   │  │  │  ├── Admin-Login.feature
│   │   │  │  │   └── Login-Negative.feature
│   │   │  │  │   └── Login-Positive.feature
│   │   │  ├── config.properties
│   │   │  ├── extent.properties
│   │   │  ├── log4j2.xml
│   │   │  ├── log4j2-back.xml
│   │   │  ├── spark-config.xml 
│   │   ├── test
│   │   │  ├── Java
│   │   │  │ ├── Page
│   │   │  │ │ ├── AdminPage.java
│   │   │  │ │ │  └── HomePage.java
│   │   │  │ │ │  └── LoginPage.java
│   │   │  │ │ │  └── NewUser.java
│   │   │  │ ├── Runner
│   │   │  │ │ ├── TestRunner.java
│   │   │  │ ├── stepdefinitions
│   │   │  │ │ ├── AdminSteps.java
│   │   │  │ │ │  └── Hooks.java
│   │   │  │ │ │  └── LoginSteps.java
│   │   │  │ │ │  └── NewUserSteps.java
│   │   │  │ ├── target
│   │   │  │ │ ├── cucumber-reports            
│   │   │  │ │ │ ├── cucumber.html           # HTML Report
│   │   │  │ │ │ │   └── cucumber.json
│   │   │  │ │ │ │   └── cucumber.xml
│   │   │  │ │ ├── ExtentReports 
│   │   │  │ │ │ ├── ExtentReport1.html      # Extent HTML Report
├── pom.xml

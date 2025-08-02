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

# Report HTML

-- ExtentReport1.html     # Extent Report
  -- Cucumber- Report
   -- Cucumber.html
     -- Cucumber.Json
  

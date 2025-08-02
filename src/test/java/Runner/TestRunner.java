package Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {
                "src/main/resources/Features/Login-Negative.feature",
                "src/main/resources/Features/Login-Positive.feature",
                "src/main/resources/Features/Admin-Login.feature"
        },
        glue = {"stepdefinitions"}, // Now includes hooks
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "junit:target/cucumber-reports/cucumber.xml"
        },
       tags = "@PositiveFlow or @Regression or @NegativeFlow or @NewUser",
        monochrome = true,
        publish = true
)
public class TestRunner {
}

package stepdefinitions;
import WebUtils.LoadProperties;
import com.aventstack.extentreports.service.ExtentService; 
import genericWrapper.GenericWrapper; 
import io.cucumber.java.After; 
import io.cucumber.java.Before;
import io.cucumber.java.Scenario; 
import org.apache.commons.io.FileUtils; 
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot; 
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import java.io.File; 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Hooks extends GenericWrapper { 
    
    private static boolean isSystemInfoSet = false;
    
    @Before
    public void setUp(Scenario scenario) {
        System.out.println("Hooks @Before - Scenario: " + scenario.getName());

        if (!isSystemInfoSet) {
            ExtentService.getInstance().setSystemInfo("OS", System.getProperty("os.name"));
            ExtentService.getInstance().setSystemInfo("Java Version", System.getProperty("java.version"));
            ExtentService.getInstance().setSystemInfo("User", System.getProperty("user.name"));
            ExtentService.getInstance().setSystemInfo("Browser", LoadProperties.get("browser"));
            isSystemInfoSet = true;
        }
        
        String browser = LoadProperties.get("browser");
        System.out.println("Hooks Initializing browser: " + browser);
        GenericWrapper.initializeDriver(browser);
        GenericWrapper.maximizeWindow();
        GenericWrapper.openUrl(LoadProperties.get("website"));
        GenericWrapper.setImplicitWait(30);
        GenericWrapper.setImplicitWait(Integer.parseInt(LoadProperties.get("implicitlyWait")));
    }

    @After
    public void tearDown(Scenario scenario) {
        System.out.println("Hooks @After - Scenario: " + scenario.getName());
        WebDriver currentDriver = GenericWrapper.getDriver();

        if (currentDriver != null) {
            try {
                if (scenario.isFailed()) {
                    System.out.println("Hooks Scenario failed. Capturing screenshot...");
                    try {
                        if (currentDriver instanceof TakesScreenshot) {
                            File screenshotFile = ((TakesScreenshot) currentDriver).getScreenshotAs(OutputType.FILE);
                            String screenshotDir = "target/ExtentReport/Screenshots";
                            File destDir = new File(screenshotDir);
                            if (!destDir.exists()) {
                                destDir.mkdirs();
                            }

                            String filename = scenario.getName().replaceAll("[^a-zA-Z0-9.\\-_]", "_") + "_" + System.currentTimeMillis() + ".png";
                            File destinationPath = new File(destDir, filename);

                            FileUtils.copyFile(screenshotFile, destinationPath);
                            byte[] fileBytes = Files.readAllBytes(Paths.get(destinationPath.getAbsolutePath()));
                            scenario.attach(fileBytes, "image/png", "Failure Screenshot: " + filename); // Attaches to Cucumber report

                            System.out.println("Screenshot saved successfully at: " + destinationPath.getAbsolutePath());
                        } else {
                            System.err.println("Current driver does not support TakesScreenshot.");
                        }
                    } catch (IOException e) {
                        System.err.println("Error saving screenshot for scenario '" + scenario.getName() + "': " + e.getMessage());
                        e.printStackTrace();
                    } catch (WebDriverException e) {
                        System.err.println("WebDriver error during screenshot capture for scenario '" + scenario.getName() + "': " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } finally {
                System.out.println("Hooks Quitting driver...");
                GenericWrapper.quitDriver();
            }
        } else {
            System.out.println("Hooks Driver was null, skipping quit.");
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

}

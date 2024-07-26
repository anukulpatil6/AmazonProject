import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;
import utils.ConfigReader;
import utils.ExcelUtil;
import utils.ScreenshotUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainTest {
    public static void main(String[] args) throws InterruptedException {
        // Initialize ConfigReader
        ConfigReader configReader = new ConfigReader();
        String browser = configReader.getProperty("browser");
        WebDriver driver = null;

        // Initialize WebDriver
        if (browser.equalsIgnoreCase("chrome")) {
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }

        if (driver != null) {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            driver.manage().window().maximize();
            driver.get(configReader.getProperty("app.url"));
            Thread.sleep(15000);
            driver.findElement(By.xpath("//button[normalize-space()='Continue shopping']")).click();
            String projectpath =System.getProperty("user.dir");
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(projectpath + "\\reports\\AmazonTestReport.html");
            ExtentReports extent = new ExtentReports();
            extent.attachReporter(htmlReporter);




//            // Test HomePage
//            HomePage homePage = new HomePage(driver);
//            homePage.clickAccountList();
//
//            // Test LoginPage
//            LoginPage loginPage = new LoginPage(driver);
//            loginPage.enterEmail("testuser@gmail.com");
//            loginPage.clickContinue();
//            loginPage.enterPassword("testpassword");
//            loginPage.clickSignIn();
//            ScreenshotUtil.captureScreenshot(driver, "LoginAttempt");
//
//            // Test ProductPage
//            homePage.searchForProduct("Laptop");
//            ProductPage productPage = new ProductPage(driver);
//            productPage.selectFirstProduct();
//            productPage.addToCart();
//            ScreenshotUtil.captureScreenshot(driver, "ProductSelected");
//
//            // Test CartPage
//            CartPage cartPage = new CartPage(driver);
//            cartPage.proceedToCheckout();
//
//            // Test CheckoutPage
//            CheckoutPage checkoutPage = new CheckoutPage(driver);
//            checkoutPage.deliverHere();
//            ScreenshotUtil.captureScreenshot(driver, "Checkout");
//
//            // Test ExcelUtil
//            ExcelUtil excelUtil = new ExcelUtil();
//            List<String[]> loginData = excelUtil.getExcelData("src/test/resources/testdata/LoginData.xlsx", "Sheet1");
//            for (String[] row : loginData) {
//                System.out.println("Email: " + row[0] + ", Password: " + row[1]);
//            }
//
            driver.quit();
        } else {
            System.out.println("Invalid browser configuration");
        }
    }
}

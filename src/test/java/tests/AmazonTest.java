package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utils.ConfigReader;
import utils.ExcelUtil;
import utils.ScreenshotUtil;

import java.time.Duration;
import java.util.List;

public class AmazonTest {

        WebDriver driver = null;
        ExtentReports extent;
        ExtentTest test;
        ConfigReader configReader;
        String projectpath =System.getProperty("user.dir");

        @BeforeClass
       public void setup() {
            configReader = new ConfigReader();
            String browser = configReader.getProperty("browser");
            if (browser.equalsIgnoreCase("chrome")) {
                driver = new ChromeDriver();
            }
            if (browser.equalsIgnoreCase("firefox")) {
                driver = new FirefoxDriver();
            }
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();
            driver.get(configReader.getProperty("app.url"));
//            try {
//                Thread.sleep(15000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            driver.findElement(By.xpath("//button[normalize-space()='Continue shopping']")).click();

            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(projectpath + "\\reports\\AmazonTestReport.html");
            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            }

            @AfterClass
            public void tearDown(){
            extent.flush();
            driver.quit();
            }

            @DataProvider(name = "loginData")
            public Object[][] getLoginData(){
                ExcelUtil excelUtil = new ExcelUtil();
                List<String[]> data = excelUtil.getExcelData(projectpath + "\\src\\test\\resources\\testdata\\LoginData.xlsx", "Sheet1");
                return data.toArray(new Object[0][]);
            }

    @DataProvider(name = "purchaseData")
    public Object[][] getPurchaseData() {
        ExcelUtil excelUtil = new ExcelUtil();
        List<String[]> data = excelUtil.getExcelData("src/test/resources/testdata/PurchaseData.xlsx", "Sheet1");
        return data.toArray(new Object[0][]);
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String email, String password) {
        test = extent.createTest("Login Test - " + email);
        HomePage homePage = new HomePage(driver);
        homePage.clickAccountList();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(email);
        loginPage.clickContinue();
        loginPage.enterPassword(password);
        loginPage.clickSignIn();
        ScreenshotUtil.captureScreenshot(driver, "LoginAttempt_" + email);
        // Add assertions to verify login success or failure
    }

    @Test(dataProvider = "purchaseData", dependsOnMethods = "testLogin")
    public void testPurchase(String productName) {
        test = extent.createTest("Purchase Test - " + productName);
        HomePage homePage = new HomePage(driver);
        homePage.searchForProduct(productName);
        ProductPage productPage = new ProductPage(driver);
        productPage.selectFirstProduct();
        productPage.addToCart();
        CartPage cartPage = new CartPage(driver);
        cartPage.proceedToCheckout();
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.deliverHere();
        ScreenshotUtil.captureScreenshot(driver, "Purchase_" + productName);
        // Add assertions to verify purchase success or failure
    }
}



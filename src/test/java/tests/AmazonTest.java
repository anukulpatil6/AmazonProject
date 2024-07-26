package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
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
import java.util.Set;

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
        List<String[]> data = excelUtil.getExcelData(projectpath + "src/test/resources/testdata/PurchaseData.xlsx", "Sheet1");
        return data.toArray(new Object[0][]);
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String email, String password) {
        ExtentTest test = extent.createTest("Login Test - " + email);
        // Open new tab for each login attempt
        ((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');");
        Set<String> handles = driver.getWindowHandles();
        String newTabHandle = handles.stream().reduce((first, second) -> second).orElse(null);
        driver.switchTo().window(newTabHandle);
        driver.get(configReader.getProperty("app.url"));

        HomePage homePage = new HomePage(driver);
        homePage.clickAccountList();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(email);
        loginPage.clickContinue();
        loginPage.enterPassword(password);
        loginPage.clickSignIn();

        if (isElementPresent(By.id("nav-link-accountList"))) {
            ScreenshotUtil.captureScreenshot(driver, "LoginSuccess_" + email);
            test.pass("Login successful for user: " + email);
            // Proceed to test other pages and functionality
            testProductSearch();
            testCartFunctionality();
            testCheckout();
        } else {
            ScreenshotUtil.captureScreenshot(driver, "LoginFailure_" + email);
            test.fail("Login failed for user: " + email);
            driver.close(); // Close the tab if login fails
            driver.switchTo().window(handles.iterator().next()); // Switch back to the original tab
        }
    }

    private void testProductSearch() {
        ExtentTest test = extent.createTest("Product Search Test");
        HomePage homePage = new HomePage(driver);
        homePage.searchForProduct("Laptop"); // Example product
        Assert.assertTrue(isElementPresent(By.cssSelector(".s-main-slot .s-result-item")));
        ScreenshotUtil.captureScreenshot(driver, "ProductSearch_Success");
        test.pass("Product search test passed.");
    }

    private void testCartFunctionality() {
        ExtentTest test = extent.createTest("Cart Functionality Test");
        ProductPage productPage = new ProductPage(driver);
        productPage.selectFirstProduct();
        productPage.addToCart();
        CartPage cartPage = new CartPage(driver);
        cartPage.proceedToCheckout();
        Assert.assertTrue(isElementPresent(By.id("checkoutButton"))); // Adjust this locator as necessary
        ScreenshotUtil.captureScreenshot(driver, "CartFunctionality_Success");
        test.pass("Cart functionality test passed.");
    }

    private void testCheckout() {
        ExtentTest test = extent.createTest("Checkout Test");
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        checkoutPage.deliverHere();
        Assert.assertTrue(isElementPresent(By.id("deliveryOptions"))); // Adjust this locator as necessary
        ScreenshotUtil.captureScreenshot(driver, "Checkout_Success");
        test.pass("Checkout test passed.");
    }

    private boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
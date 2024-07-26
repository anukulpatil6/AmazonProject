package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private WebDriver driver;
    private By accountList = By.id("nav-link-accountList-nav-line-1");
    private By searchBox = By.id("twotabsearchtextbox");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickAccountList() {
        driver.findElement(accountList).click();
    }

    public void searchForProduct(String productName) {
        driver.findElement(searchBox).sendKeys(productName);
        driver.findElement(By.id("nav-search-submit-button")).click();
    }
}

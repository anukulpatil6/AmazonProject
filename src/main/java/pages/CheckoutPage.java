package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {
    private WebDriver driver;
    private By deliverHereButton = By.name("deliverHere");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public void deliverHere() {
        driver.findElement(deliverHereButton).click();
    }
}

package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenshotUtil {
    public static void captureScreenshot(WebDriver driver, String screenshotName){
        File src = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File destination = new File("./screenshots/" + screenshotName + ".png");
        try {
            FileUtils.copyFile(src,destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

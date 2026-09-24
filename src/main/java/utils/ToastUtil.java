package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * 获取当前屏幕的土司的文本
 * 返回土司的文本
 */


public class ToastUtil {
    private static final By TOAST_LOCATOR = By.xpath("//*[@class='android.widget.Toast']");
    public static String getToastText(AppiumDriver driver,int timeoutSeconds){
     try {
         WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(timeoutSeconds));
         WebElement toast =wait.until(ExpectedConditions.presenceOfElementLocated(TOAST_LOCATOR));
         return toast.getText();
     }catch (Exception e){
         System.out.println("未在规定时间内找到Toast元素");
         return "";
     }
    }
    /**
     * 判断Toast是否出现，并包含指定文本
     */

    public static boolean isToastDisplayed(AppiumDriver driver, String expectedText, int timeoutSeconds) {
        String actualText = getToastText(driver, timeoutSeconds);
        return actualText != null && actualText.contains(expectedText);
    }


}

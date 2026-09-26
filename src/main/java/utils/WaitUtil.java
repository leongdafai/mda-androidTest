package utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * 显式等待工具
 * 作用：让代码"等元素出现"，而不是"立刻找，找不到就报错"
 */
public class WaitUtil {

    // 默认超时 15 秒
    private static final int DEFAULT_TIMEOUT = 15;

    /**
     * 等元素出现（可见），出现就返回元素
     * 找不到就抛 TimeoutException
     */
    public static WebElement waitForElement(AppiumDriver driver, By locator) {
        return waitForElement(driver, locator, DEFAULT_TIMEOUT);
    }

    public static WebElement waitForElement(AppiumDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        // visibilityOfElementLocated：等元素出现在页面上且可见
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * 等元素可点击
     */
    public static WebElement waitForClickable(AppiumDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * 等元素消失（不可见或不存在）
     */
    public static boolean waitForInvisible(AppiumDriver driver, By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

}
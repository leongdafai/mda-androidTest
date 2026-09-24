package utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

/**
 * 滑动工具类
 * 封装上下左右滑动、按比例滑动、滚动到指定文本
 */
public class SwipeUtil {

    /** 默认滑动时长（毫秒），太短会被识别成 fling，太长会变慢 */
    private static final int DEFAULT_DURATION = 600;

    /** 向上滑（页面向下滚，看下方内容） */
    public static void swipeUp(AppiumDriver driver) {
        swipe(driver, 0.5, 0.8, 0.5, 0.3);
    }

    /** 向下滑（页面向上滚，看上方内容） */
    public static void swipeDown(AppiumDriver driver) {
        swipe(driver, 0.5, 0.3, 0.5, 0.8);
    }

    /** 向左滑 */
    public static void swipeLeft(AppiumDriver driver) {
        swipe(driver, 0.8, 0.5, 0.3, 0.5);
    }

    /** 向右滑 */
    public static void swipeRight(AppiumDriver driver) {
        swipe(driver, 0.3, 0.5, 0.8, 0.5);
    }

    /**
     * 通用滑动：按屏幕比例从起点滑到终点
     *
     * @param startXRatio 起点 X 比例（0~1）
     * @param startYRatio 起点 Y 比例（0~1）
     * @param endXRatio   终点 X 比例（0~1）
     * @param endYRatio   终点 Y 比例（0~1）
     */
    public static void swipe(AppiumDriver driver,
                             double startXRatio, double startYRatio,
                             double endXRatio, double endYRatio) {
        // 1. 拿屏幕尺寸，按比例算实际坐标
        Dimension size = driver.manage().window().getSize();
        int startX = (int) (size.width * startXRatio);
        int startY = (int) (size.height * startYRatio);
        int endX = (int) (size.width * endXRatio);
        int endY = (int) (size.height * endYRatio);

        // 2. W3C Actions：手指移到起点 → 按下 → 移到终点 → 抬起
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(DEFAULT_DURATION),
                PointerInput.Origin.viewport(), endX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }

    /**
     * 滚动到指定文本的元素（Android 专用，比手动滑动更可靠）
     * 用途：列表里找某个商品、某个选项
     */
    public static void scrollToText(AppiumDriver driver, String text) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
    }
}
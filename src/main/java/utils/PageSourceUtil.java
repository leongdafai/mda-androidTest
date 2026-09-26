package utils;

import io.appium.java_client.AppiumDriver;

public class PageSourceUtil {

    /**
     * 获取当前页面的 XML 源码
     * 页面源码就是 Inspector 里看到的那棵元素树
     * Appium 自带的，返回当前页面的完整 XML
     */
    public static String getPageSource(AppiumDriver driver) {
        return driver.getPageSource();
    }
}
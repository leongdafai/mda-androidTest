package base;

import config.CapabilityLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class DriverFactory {
    //DriverFactory 根据平台（Android/iOS），读取对应配置，创建一个 Driver 对象，设置隐式等待，返回给调用者。
    private static final String APPIUM_SERVER_URL = "http://127.0.0.1:4723";
    private static final String APP_PACKAGE = "com.joker.coolmall";

    public static AppiumDriver initDriver(String platform) {
        //1,找配置文件路径
        String configFile = "capabilities/" + platform.toLowerCase() + ".json";
        //2，读取，拿到capability
        Capabilities caps = CapabilityLoader.load(configFile,platform);
        //3，准备appium server 地址；
        if ("android".equalsIgnoreCase(platform)) {
//            clearAppData(APP_PACKAGE);
        }

        URL serverUrl;
        try {
            serverUrl = new URL(APPIUM_SERVER_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Appium Server 地址格式不对: " + APPIUM_SERVER_URL, e);
        }
        // 第 4 步：根据平台创建对应的 Driver
        AppiumDriver driver;
        if ("android".equalsIgnoreCase(platform)) {
            driver = new AndroidDriver(serverUrl, caps);
        } else if ("ios".equalsIgnoreCase(platform)) {
            driver = new IOSDriver(serverUrl, caps);
        } else {
            throw new IllegalArgumentException("不支持的平台: " + platform);
        }
        // 第 5 步：设置隐式等待
        // 意思：找元素时如果找不到，最多等 10 秒再报错
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        return driver;
    }
    private static void clearAppData(String packageName) {
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[]{"adb", "shell", "pm", "clear", packageName});
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("清空 App 数据失败: " + packageName, e);
        }
    }
    public static void main(String[] args) {
        AppiumDriver driver = DriverFactory.initDriver("android");
        System.out.println("Driver 创建成功！");

        // 强转成 AndroidDriver，才能调用 Android 特有的方法
        AndroidDriver androidDriver = (AndroidDriver) driver;
        System.out.println("当前 App 包名: " + androidDriver.getCurrentPackage());
        System.out.println("当前 Activity: " + androidDriver.currentActivity());

        // 用完要退出
        driver.quit();
        System.out.println("Driver 已退出");
    }
}
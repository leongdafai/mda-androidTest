package base;

import config.CapabilityLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Capabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

public class DriverFactory {

    private static final int[] SERVER_PORTS = {4724};
    private static final String[] CONFIG_FILES = {
            "capabilities/android2.json"
    };
    private static final AtomicInteger PORT_INDEX = new AtomicInteger(0);

    public static AppiumDriver initDriver(String platform) {
        int index = PORT_INDEX.getAndIncrement() % SERVER_PORTS.length;
        int port = SERVER_PORTS[index];
        String configFile = CONFIG_FILES[index];
        String serverUrl = "http://127.0.0.1:" + port;
        System.out.println(">>> 当前线程: Server=" + serverUrl + ", Config=" + configFile);

        Capabilities caps = CapabilityLoader.load(configFile, platform);

        URL url;
        try {
            url = new URL(serverUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Appium Server 地址格式错误: " + serverUrl, e);
        }

        AppiumDriver driver;
        if ("android".equalsIgnoreCase(platform)) {
            driver = new AndroidDriver(url, caps);
        } else if ("ios".equalsIgnoreCase(platform)) {
            driver = new IOSDriver(url, caps);
        } else {
            throw new IllegalArgumentException("不支持的平台: " + platform);
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }
}
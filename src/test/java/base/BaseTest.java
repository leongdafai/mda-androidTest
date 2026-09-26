package base;

import base.DriverFactory;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.PageSourceUtil;
import utils.ScreenshotUtil;

import java.io.ByteArrayInputStream;
import java.util.Map;

@Listeners({AllureTestNg.class})
public class BaseTest {
    protected AppiumDriver driver;
    @BeforeClass(alwaysRun = true)
    @Parameters("platform")
    public void setUp(@Optional("android") String platform) {
        driver = DriverFactory.initDriver(platform);
}

    @BeforeMethod
    public void resetApp() {
        // 不重建 Driver，只重启 App 到首页
        try {
            Runtime.getRuntime().exec(new String[]{
                    "adb", "shell", "am", "force-stop", "com.joker.coolmall"
            }).waitFor();
            Thread.sleep(2000);
            driver.executeScript("mobile: activateApp",
                    java.util.Map.of("appId", "com.joker.coolmall"));
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void captureOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // 截图的字节流，直接挂到 Allure 报告
            byte[] screenshot = ScreenshotUtil.takeScreenshotAsBytes(driver);
            Allure.addAttachment(
                    "失败截图",
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    "png"
            );
            String pageSource = PageSourceUtil.getPageSource(driver);
            Allure.addAttachment("页面源码", "text/xml",
                    pageSource, "xml");
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}

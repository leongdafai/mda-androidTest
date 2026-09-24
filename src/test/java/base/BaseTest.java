package base;

import base.DriverFactory;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ScreenshotUtil;

import java.io.ByteArrayInputStream;
@Listeners({AllureTestNg.class})
public class BaseTest {
    protected AppiumDriver driver;
    @BeforeClass(alwaysRun = true)
    @Parameters("platform")
    public void setUp(@Optional("android") String platform) {
        driver = DriverFactory.initDriver(platform);
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

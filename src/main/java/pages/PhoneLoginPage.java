package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.interactions.PointerInput;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Sequence;
import utils.WaitUtil;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

public class PhoneLoginPage extends BasePage {
    private static final By REGISTER_BUTTON = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Register\")");

    public PhoneLoginPage(AppiumDriver driver) { super(driver); }

    public RegisterPage goToRegister() {
        // 关键：先隐藏键盘，恢复布局

        click(REGISTER_BUTTON);
        try { Thread.sleep(2000); } catch (Exception e) {}

        return new RegisterPage(driver);


    }

    public boolean isLoaded() {
        return isDisplayed(REGISTER_BUTTON);
    }


}


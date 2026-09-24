package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class ProfilePage extends BasePage {
    private static final By PROFILE_INFO = AppiumBy.androidUIAutomator(
            "new UiSelector().className(\"android.view.View\").instance(5)");

    public ProfilePage(AppiumDriver driver) { super(driver); }

    public LoginEntryPage goToLoginEntry() {
        click(PROFILE_INFO);
        return new LoginEntryPage(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(PROFILE_INFO); // 或者用其他元素
    }
}

package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class LoginEntryPage extends BasePage {
    private static final By ACCOUNT_LOGIN = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.Button\").instance(1)");

    public LoginEntryPage(AppiumDriver driver) { super(driver); }

    public PhoneLoginPage goToPhoneLogin() {
        click(ACCOUNT_LOGIN);
        return new PhoneLoginPage(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(ACCOUNT_LOGIN);
    }
}

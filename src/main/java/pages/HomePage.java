package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class HomePage extends BasePage {
    private static final By SEARCH_ICON = AppiumBy.accessibilityId("Search");
    private static final By ME_ICON = AppiumBy.androidUIAutomator("new UiSelector().text(\"Me\")");
    private static final By MOBILE_ICON = AppiumBy.androidUIAutomator("new UiSelector().text(\"手机\")");



    public HomePage(AppiumDriver driver){super(driver);}

    public SearchPage goToSearch(){
        click(SEARCH_ICON);
        return new SearchPage(driver);}

    public ProfilePage goToProfile() {
        click(ME_ICON);
        return new ProfilePage(driver);
    }

    public ResultPage goToFirstTab(){click(MOBILE_ICON);return new ResultPage(driver);}



    public boolean isLoaded() {
        return isDisplayed(SEARCH_ICON);
    }

}

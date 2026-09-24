package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

public class FilterPage extends BasePage {
    private static final By MIN_PRICE_INPUT=AppiumBy.androidUIAutomator("new UiSelector().text(\"Min\")");
    private static final By MAX_PRICE_INPUT=AppiumBy.androidUIAutomator("new UiSelector().text(\"Max\")");
    private static final By RESET_BUTTON=AppiumBy.androidUIAutomator("new UiSelector().text(\"Reset\")");
    private static final By CONFIRM_BUTTON=AppiumBy.androidUIAutomator("new UiSelector().text(\"Confirm\")");

    public FilterPage(AppiumDriver driver){super(driver);}




    public FilterPage setPriceRange(String min,String max){
        click(MIN_PRICE_INPUT);new Actions(driver).sendKeys("1234").perform();
        click(MAX_PRICE_INPUT);new Actions(driver).sendKeys("5678").perform();
        return this;
    }

    public FilterPage selectCategory (String categoryName){
        By categoryLocator = AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + categoryName + "\")");
        click(categoryLocator);
        return this;
    }

    public FilterPage clickReset() {
        click(RESET_BUTTON);
        return this;
    }

    /** 点击确认，应用筛选并关闭弹窗，返回结果页 */
    public ResultPage clickConfirm() {
        click(CONFIRM_BUTTON);
        return new ResultPage(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(CONFIRM_BUTTON);
    }
}


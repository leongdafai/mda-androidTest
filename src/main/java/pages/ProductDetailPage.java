package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class ProductDetailPage extends BasePage {
    //页面加载识别根据：购物车 new UiSelector().text("Cart")
    private static final By CARTICON = AppiumBy.androidUIAutomator("new UiSelector().text(\"Cart\")");

    public ProductDetailPage(AppiumDriver driver){super(driver);}

    public boolean isLoaded() {
        return isDisplayed(CARTICON);
    }

}

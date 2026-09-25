package pages;

import base.BasePage;
import base.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import utils.SwipeUtil;
import utils.WaitUtil;

public class ResultPage extends BasePage {
    private static final By VIEW_BUTTON=AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").instance(13)");
    private static final By SEARCH_INPUT =AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").instance(18)");
    private static final By SEARCH_BUTTON_TEXT = AppiumBy.androidUIAutomator("new UiSelector().text(\"Search\")");
    private static final By FLITER_ICON=AppiumBy.androidUIAutomator("new UiSelector().text(\"Filter\")");
    private static final By FIRST_PRODUCT_POSITION=AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").instance(8)");
    private static final By FIRST_PRODUCT = AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").instance(10)");   // 第一个商品卡片
    private static final By NO_RESULT_TIP = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"No content available\")");

    public ResultPage(AppiumDriver driver){super(driver);}

    //筛选后弹出的窗口。
    public FilterPage openFilter() {
        click(FLITER_ICON);
        return new FilterPage(driver);
    }


    public ResultPage searchProduct(String productName){
        WaitUtil.waitForClickable(driver,SEARCH_INPUT);
        click(SEARCH_INPUT); //激活对话框
        Actions action = new Actions(driver);
        action.sendKeys(productName).perform();//actions类模拟键盘输入
        click(SEARCH_BUTTON_TEXT);
        return new ResultPage(driver);
    }

    public ProductDetailPage tapFirstProduct(){
        click(FIRST_PRODUCT_POSITION);
        return new ProductDetailPage(driver);
    }

    public boolean isLoaded(){return isDisplayed(VIEW_BUTTON);}
    public boolean hasAnyProduct() {
        return isDisplayed(FIRST_PRODUCT);
    }

    public boolean isNoResultTipDisplayed() {
        return isDisplayed(NO_RESULT_TIP);
    }
}

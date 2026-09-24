package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;

import javax.xml.transform.Result;

public class SearchPage extends BasePage {
    private static final By SEARCH_BUTTON_TEXT = AppiumBy.androidUIAutomator("new UiSelector().text(\"Search\")");
    private static final By SEARCH_INPUT =AppiumBy.androidUIAutomator("new UiSelector().className(\"android.view.View\").instance(35)");


    public SearchPage(AppiumDriver driver){super(driver);}


    public ResultPage searchProduct(String productName){
        click(SEARCH_INPUT); //激活对话框
        Actions action = new Actions(driver);
        action.sendKeys(productName).perform();//actions类模拟键盘输入

        click(SEARCH_BUTTON_TEXT);
        return new ResultPage(driver);
    }


    public boolean isLoaded(){
        return isDisplayed(SEARCH_BUTTON_TEXT);
    }



}

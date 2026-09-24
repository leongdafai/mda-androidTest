package pages;

import base.BasePage;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;

public class LoginPage extends BasePage {
    private static final By USERNAME_FIELD = AppiumBy.id("com.saucelabs.mydemoapp.android:id/nameET");
    private static final By PASSWORD_FIELD =AppiumBy.id("com.saucelabs.mydemoapp.android:id/passwordET");
    private static final By LOGIN_BUTTON = AppiumBy.id("com.saucelabs.mydemoapp.android:id/loginBtn");

public LoginPage(AppiumDriver driver){super(driver);}


public LoginPage enterUsername(String username){
    type(USERNAME_FIELD,username);
    return this;
}
public LoginPage enterPassword(String password){
    type(PASSWORD_FIELD,password);
    return this;
}

public HomePage tapLogin(){
    click(LOGIN_BUTTON);
    return new HomePage(driver);
}

public boolean isLoaded(){
    return isDisplayed(USERNAME_FIELD);
}







}

package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.RegisterPage;

public class RegisterTest extends BaseTest {


        @Test(description = "从首页导航到注册页并完成注册")
        public void testRegister() throws Exception {
            RegisterPage registerPage = new HomePage(driver)
                    .goToProfile()
                    .goToLoginEntry()
                    .goToPhoneLogin()
                    .goToRegister();

            // 一行调用完整流程
            registerPage.register("13800138000", "Test1234");

            Assert.assertTrue(registerPage.isLoaded(), "注册流程完成");
        }
    }
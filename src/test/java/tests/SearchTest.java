package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import pages.ResultPage;
import pages.SearchPage;


public class SearchTest extends BaseTest {
    @Test(description = "成功搜索")
    public void testSearch() {
        // 1. 首页 → 点搜索 → 输入点确认 → 进入搜索页
        ResultPage resultPage = new HomePage(driver)
                .goToSearch().searchProduct("Xiaomi 17 Pro");


        // 2. 验证确实到了登录页
        Assert.assertTrue(resultPage.isLoaded(), "已进入搜索页");

    }

}

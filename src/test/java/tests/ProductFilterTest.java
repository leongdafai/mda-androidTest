package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ResultPage;

public class ProductFilterTest extends BaseTest {
    //首页--点击导航栏第一个图标--点击筛选按钮--输入价格区间确认--弹出结果
    @Test(description = "商品筛选完成")
    public void testProductFilter(){
        ResultPage resultPage = new HomePage(driver).goToFirstTab().openFilter().setPriceRange("3000","4000").clickConfirm();
        Assert.assertTrue(resultPage.isLoaded(),"已筛选出结果");
    }
}

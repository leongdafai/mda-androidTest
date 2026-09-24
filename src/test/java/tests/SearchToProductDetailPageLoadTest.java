package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.ResultPage;

/**
 * 搜索 → 结果页点第一个商品 → 详情页 → 断言加载
 */
public class SearchToProductDetailPageLoadTest extends BaseTest {

    @Test(description = "搜索商品后点击第一个结果，应进入详情页")
    public void  testSearchToDetail(){
        ProductDetailPage productDetailPage = new HomePage(driver).goToSearch().searchProduct("Xiaomi 17 Pro").tapFirstProduct();
        Assert.assertTrue(productDetailPage.isLoaded(),"已进入商品详情页");
    }


}

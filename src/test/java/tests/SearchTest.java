    package tests;

    import base.BaseTest;
    import org.testng.Assert;
    import org.testng.annotations.DataProvider;
    import org.testng.annotations.Test;
    import pages.HomePage;
    import pages.LoginPage;
    import pages.ResultPage;
    import pages.SearchPage;
    import utils.ExcelUtil;


    public class SearchTest extends BaseTest {

        @DataProvider(name = "searchData")
        public Object[][] searchData() {
            return ExcelUtil.readExcel("testdata/search.xlsx", "Sheet1");
        }


        @Test(dataProvider = "searchData", description = "搜索不同关键词,结果符合预期")
        public void testSearch(String keyword,String expected) {
            // 1. 首页 → 点搜索 → 输入点确认 → 进入搜索页
            ResultPage resultPage = new HomePage(driver)
                    .goToSearch().searchProduct(keyword);

            boolean shouldHaveResult = Boolean.parseBoolean(expected);
            // 2. 验证确实到了登录页
            if (shouldHaveResult) {
                Assert.assertTrue(resultPage.hasAnyProduct(),
                        "关键词 [" + keyword + "] 应有结果");
            } else {
                Assert.assertTrue(resultPage.isNoResultTipDisplayed(),
                        "关键词 [" + keyword + "] 应显示'No content available'");
            }

        }

    }

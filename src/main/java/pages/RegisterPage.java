package pages;

import base.BasePage;
import base.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.OcrUtil;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPage extends BasePage {

    private static final By SMS_CODE_INPUT = AppiumBy.androidUIAutomator("new UiSelector().text(\"Verification code\")");
    // 注册按钮
    private static final By REGISTER_BUTTON = AppiumBy.androidUIAutomator("new UiSelector().text(\"Register\")");

    // 号码框
    private static final By PHONE_NUM_BOX = AppiumBy.androidUIAutomator(
            "new UiSelector().className(\"android.widget.EditText\").instance(0)");

    // 第一个密码框
    private static final By PASSWORD_FIELD_1 = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Please set password\")");

    // 第二个确认密码框
    private static final By PASSWORD_FIELD_2 = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Please confirm password\")");

    // 发送验证码按钮（弹窗出现前）
    private static final By SEND_BUTTON = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Get code\")");

    // 验证码图片（弹窗里）
    private static final By CAPTCHA_IMAGE = AppiumBy.accessibilityId("Captcha image");

    // 验证码输入框（弹窗里）
    private static final By CAPTCHA_INPUT = AppiumBy.androidUIAutomator("new UiSelector().text(\"Please enter captcha code\")");

    // 验证码弹窗的确认按钮
    private static final By SEND_CODE_BUTTON = AppiumBy.androidUIAutomator("new UiSelector().text(\"Complete Verification\")");



    public RegisterPage(AppiumDriver driver) {
        super(driver);
    }

    /** 输入手机号 */
    public RegisterPage enterPhoneNumber(String phoneNumber) {
        click(PHONE_NUM_BOX);
        new Actions(driver).sendKeys(phoneNumber).perform();
        return this;
    }

    /** 输入密码 */
    public RegisterPage enterPassword(String password) {
        click(PASSWORD_FIELD_1);
        new Actions(driver).sendKeys(password).perform();

        return this;
    }

    /** 输入确认密码（第二次） */
    public RegisterPage enterConfirmPassword(String password) {
        click(PASSWORD_FIELD_2);
        new Actions(driver).sendKeys(password).perform();

        return this;
    }

    /** 点击"Get code"，触发验证码弹窗 */
    public RegisterPage tapSendButton() {
        click(SEND_BUTTON);
        return this;
    }

    public RegisterPage tapSMSInput(){
        click(CAPTCHA_INPUT);
        new Actions(driver).sendKeys("1234").perform();
        click(SEND_CODE_BUTTON);
        return this;
    }


    /**
     * 处理验证码弹窗：
     * 截图 → OCR识别 → 计算答案 → 填入 → 点确认
     */
    public RegisterPage handleCaptcha() throws Exception {
        // 1. 截图验证码图片（只截那个元素的区域）
        byte[] imageBytes = ((TakesScreenshot) find(CAPTCHA_IMAGE))
                .getScreenshotAs(OutputType.BYTES);
        java.nio.file.Files.write(
                java.nio.file.Paths.get("captcha_debug.png"), imageBytes);
        System.out.println(">>> 截图已保存到 captcha_debug.png");


        // 2. 调 OCR 识别
        String text = OcrUtil.recognize(imageBytes);
        System.out.println("OCR识别结果：" + text);

        // 3. 计算答案
        String answer = calculate(text);
        System.out.println("计算答案：" + answer);

        // 4. 填入验证码输入框
        click(CAPTCHA_INPUT);
        new Actions(driver).sendKeys(answer).perform();

        // 5. 点击确认
        click(SEND_CODE_BUTTON);

        return this;
    }
    private static final int MAX_RETRY = 3;

    public RegisterPage handleCaptchaWithRetry() throws Exception {
        for (int i = 1; i <= MAX_RETRY; i++) {
            System.out.println(">>> 验证码识别第 " + i + " 次尝试");
            try {
                // 1. 截图 + OCR
                byte[] imageBytes = ((TakesScreenshot) find(CAPTCHA_IMAGE))
                        .getScreenshotAs(OutputType.BYTES);
                String text = OcrUtil.recognize(imageBytes);
                System.out.println(">>> OCR 结果：[" + text + "]");   // 用方括号包住，能看出空串

                // 2. ★ 统一校验：必须恰好是 4 位纯数字
                //    空字符串、含字母、多于/少于4位，都会被拦下
                if (text == null || !text.matches("\\d{4}")) {
                    System.out.println(">>> 识别结果不是 4 位数字，点击图片刷新后重试");
                    click(CAPTCHA_IMAGE);
                    Thread.sleep(1000);
                    continue;
                }

                // 3. 填答案 + 点确认
                click(CAPTCHA_INPUT);
                new Actions(driver).sendKeys(text).perform();
                click(SEND_CODE_BUTTON);

                // 4. 等错误提示
                if (!isCaptchaError()) {
                    System.out.println(">>> 验证码通过");
                    return this;
                }

                // 5. 填错了，刷新重试
                System.out.println(">>> 验证码错误，点击图片刷新后重试");
                Thread.sleep(2500);
                click(CAPTCHA_IMAGE);
                Thread.sleep(1000);

            } catch (Exception e) {
                System.out.println(">>> 第 " + i + " 次失败：" + e.getMessage());
                if (i == MAX_RETRY) {
                    throw new RuntimeException("验证码识别重试 " + MAX_RETRY + " 次仍失败", e);
                }
                Thread.sleep(2000);
            }
        }
        throw new RuntimeException("验证码识别重试 " + MAX_RETRY + " 次仍失败");
    }

    /**
     * 判断是否出现"验证码错误"提示
     * 短超时（3 秒），因为 Toast 2 秒后会消失
     */
    private boolean isCaptchaError() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.androidUIAutomator("new UiSelector().textContains(\"错误\")")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * 解析 OCR 返回的算术题并算出答案
     * 支持格式："3+8=?"、"3+8="、"3+8"、"3 + 8 = ?"、"3+8=11"
     */
    private String calculate(String text) {
        // 去掉空格、问号、等号
        String expr = text.replaceAll("[\\s=?？]", "");

        // 如果包含加法
        if (expr.contains("+")) {
            String[] parts = expr.split("\\+");
            return String.valueOf(Integer.parseInt(parts[0]) + Integer.parseInt(parts[1]));
        }
        // 如果包含减法
        if (expr.contains("-")) {
            String[] parts = expr.split("-");
            return String.valueOf(Integer.parseInt(parts[0]) - Integer.parseInt(parts[1]));
        }
        // 如果包含乘法
        if (expr.contains("×") || expr.contains("*")) {
            String[] parts = expr.split("[×*]");
            return String.valueOf(Integer.parseInt(parts[0]) * Integer.parseInt(parts[1]));
        }
        // 如果只有单个数字
        return expr;
    }
    /**
     * 从通知栏读取短信验证码
     * 返回 4~6 位数字
     */
    public String getOtpFromNotification() {
        // 1. 打开通知栏
        driver.executeScript("mobile: openNotifications");

        // 2. 等通知加载
        try { Thread.sleep(2000); } catch (Exception e) {}

        // 3. 拿所有通知文本
        List<WebElement> notifications = driver.findElements(
                AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.TextView\")"));

        String otp = null;
        for (WebElement n : notifications) {
            String text = n.getText();
            // 用正则匹配 4~6 位数字
            Matcher matcher = Pattern.compile("\\d{4,6}").matcher(text);
            if (matcher.find()) {
                otp = matcher.group();
                break;
            }
        }

        // 4. 关闭通知栏（按返回键）
        driver.navigate().back();

        if (otp == null) {
            throw new RuntimeException("通知栏里没有找到验证码");
        }
        return otp;
    }

//    public RegisterPage register(String phone, String password) throws Exception {
//        // 1~3. 输手机号、密码、确认密码
//        enterPhoneNumber(phone);
//        enterPassword(password);
//        enterConfirmPassword(password);
//
//        // 4. 点 Get code
//        tapSendButton();
//
//        // 5~6. 处理图形验证码弹窗
//        handleCaptcha();
//
//        // 7~8. 从通知栏读短信验证码
//        String otp = getOtpFromNotification();
//
//        // 9. 填入短信验证码
//        click(SMS_CODE_INPUT);
//        new Actions(driver).sendKeys(otp).perform();
//
//        // 10. 点注册
//        click(REGISTER_BUTTON);
//
//        return this;
//    }
public RegisterPage register(String phone, String password) throws Exception {
    // 1. 填手机号
    enterPhoneNumber(phone);

    // 2. 填密码
    enterPassword(password);

    // 3. 填确认密码
    enterConfirmPassword(password);

    // 4. 点 Get code，弹出图形验证码
    tapSendButton();

    // 5. 处理图形验证码（带重试）
    handleCaptchaWithRetry();

    // 6. 等短信到达（5秒）
    Thread.sleep(5000);

    // 7. ★ 从通知栏读短信验证码（新加的）
    String smsCode = getSmsCodeFromNotification();
    System.out.println(">>> 拿到短信验证码：" + smsCode);

    // 8. ★ 填入短信验证码
    click(SMS_CODE_INPUT);
    new Actions(driver).sendKeys(smsCode).perform();

    // 9. 点注册
    click(REGISTER_BUTTON);

    return this;
}
    public boolean isLoaded() {
        return isDisplayed(PHONE_NUM_BOX);
    }

    public String getSmsCodeFromNotification() {
        try {
            // 1. 获取所有通知（注意：最外层是 Map，不是 List）
            Map<String, Object> result = (Map<String, Object>) driver.executeScript("mobile: getNotifications");
            List<Map<String, Object>> notifications = (List<Map<String, Object>>) result.get("statusBarNotifications");

            if (notifications == null || notifications.isEmpty()) {
                throw new RuntimeException("通知栏为空");
            }

            Pattern pattern = Pattern.compile("\\b\\d{4}\\b");

            // 2. 遍历通知，过滤出青商城的通知
            for (Map<String, Object> notification : notifications) {
                String packageName = (String) notification.get("packageName");
                System.out.println(">>> 通知来源包名：" + packageName);

                // 只处理青商城的通知
                if (!"com.joker.coolmall".equals(packageName)) {
                    continue;
                }

                // 3. 解析 notification 对象（里面才是真正的标题和内容）
                Map<String, Object> notifContent = (Map<String, Object>) notification.get("notification");
                if (notifContent == null) continue;

                String title = (String) notifContent.get("title");
                String text = (String) notifContent.get("text");
                String bigText = (String) notifContent.get("bigText");
                String bigTitle = (String) notifContent.get("bigTitle");

                System.out.println(">>> title: " + title);
                System.out.println(">>> text: " + text);
                System.out.println(">>> bigText: " + bigText);

                // 4. 拼接所有可能包含验证码的字段
                String fullText = String.join(" ",
                        title != null ? title : "",
                        text != null ? text : "",
                        bigText != null ? bigText : "",
                        bigTitle != null ? bigTitle : "");

                // 5. 正则匹配 4 位数字
                Matcher matcher = pattern.matcher(fullText);
                if (matcher.find()) {
                    String code = matcher.group();
                    System.out.println(">>> 找到验证码：" + code);
                    return code;
                }
            }

        } catch (Exception e) {
            System.err.println(">>> 读取通知失败：" + e.getMessage());
        }
        throw new RuntimeException("未能在通知中找到 4 位验证码");
    }

    public static void main(String[] args) throws Exception {
        AppiumDriver driver = DriverFactory.initDriver("android");


        RegisterPage registerPage = new HomePage(driver)
                .goToProfile()
                .goToLoginEntry()
                .goToPhoneLogin()
                .goToRegister();

        registerPage.register("13800138000", "Test1234");

        // 断言：注册后应该跳转到了某个页面（根据实际 UI 改）

    }


}
package config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.Capabilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

///**
// * Capability 配置加载器
// */
public class CapabilityLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 加载 Capability 配置文件，返回对应平台的 Options 对象
     *
     * @param platform 平台，传 "android" 或 "ios"
     * @return Capabilities 对象（实际上是 UiAutomator2Options 或 XCUITestOptions）
     */
    public static Capabilities load(String filePath, String platform) {
        Map<String, Object> config;

        try (InputStream is = CapabilityLoader.class.getClassLoader()
                .getResourceAsStream(filePath)) {
            if (is == null) {
                throw new IllegalArgumentException("找不到配置文件: " + filePath);
            }
            config = MAPPER.readValue(is, new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            throw new RuntimeException("读取 Capability 文件失败: " + filePath, e);
        }

        // 根据平台创建对应的 Options 对象
        if ("android".equalsIgnoreCase(platform)) {
            UiAutomator2Options options = new UiAutomator2Options();
            config.forEach(options::setCapability);
            return options;
        } else if ("ios".equalsIgnoreCase(platform)) {
            XCUITestOptions options = new XCUITestOptions();
            config.forEach(options::setCapability);
            return options;
        } else {
            throw new IllegalArgumentException("不支持的平台: " + platform);
        }
    }

    public static void main(String[] args) {
        CapabilityLoader.load("capabilities/android1.json","android");
    }

}

package utils;

import com.aliyun.ocr_api20210707.Client;
import com.aliyun.ocr_api20210707.models.RecognizeGeneralRequest;
import com.aliyun.ocr_api20210707.models.RecognizeGeneralResponse;
import com.aliyun.ocr_api20210707.models.RecognizeAdvancedRequest;
import com.aliyun.ocr_api20210707.models.RecognizeAdvancedResponse;
import com.aliyun.teaopenapi.models.Config;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;

public class OcrUtil {

    private static final String AK = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_ID");
    private static final String SK = System.getenv("ALIBABA_CLOUD_ACCESS_KEY_SECRET");
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String recognize(byte[] imageBytes) throws Exception {
        Config config = new Config()
                .setAccessKeyId(AK)
                .setAccessKeySecret(SK)
                .setEndpoint("ocr-api.cn-hangzhou.aliyuncs.com");
        Client client = new Client(config);

        RecognizeGeneralRequest request = new RecognizeGeneralRequest()
                .setBody(new ByteArrayInputStream(imageBytes));

        RecognizeGeneralResponse response = client.recognizeGeneral(request);
        String rawJson = response.getBody().getData();

        // 解析 JSON
        JsonNode root = MAPPER.readTree(rawJson);
        String content = root.get("content").asText();

        // 去空格、只留数字
        content = content.replaceAll("\\s+", "");
        content = content.replaceAll("[^0-9]", "");

        // ★ 关键：不再抛异常，空字符串直接返回，交给调用方判断
        return content;
    }
}
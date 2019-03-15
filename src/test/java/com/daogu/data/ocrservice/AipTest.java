package com.daogu.data.ocrservice;

import com.baidu.aip.ocr.AipOcr;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;

import java.util.HashMap;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
public class AipTest {
    public static final String APP_ID = "15744076";
    public static final String API_KEY = "LKmhWf5tvwORttXPFv6Kvc5I";
    public static final String SECRET_KEY = "S9e1RMh9qjT0p6YuB10sPGRFb86dUhWW";

    @Test
    public void test() {
        // 初始化一个AipOcr
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用接口
        String path = "E:/daogu-wkzone/images/1.jpg";
        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
        log.info(res.toString(2));

        String words = StreamSupport.stream(res.getJSONArray("words_result").spliterator(), false)
                .map(jsonObj -> {
                    if (jsonObj instanceof JSONObject) {
                        return ((JSONObject) jsonObj).getString("words");
                    }
                    return "";
                }).collect(Collectors.joining(""));
        log.info("words:{}", words);
    }

    @Test
    public void testReadImage() {

    }
}

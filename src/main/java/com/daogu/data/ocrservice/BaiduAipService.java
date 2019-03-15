package com.daogu.data.ocrservice;

import com.baidu.aip.ocr.AipOcr;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class BaiduAipService {

    private BaiduAipProperties aipProperties;

    private AipOcr aipOcr;

    public BaiduAipService(BaiduAipProperties aipProperties) {
        this.aipProperties = aipProperties;
        this.aipOcr = new AipOcr(aipProperties.getAppId(), aipProperties.getAppKey(), aipProperties.getSecretKey());
    }

    @Retryable(include = AipOverLimitException.class)
    public String doOcr(byte[] bytes, HashMap<String, String> options) {
        JSONObject result = this.aipOcr.basicGeneral(bytes, options);
        log.info(result.toString(-2));
        if (result.opt("error_code") != null) {
            int errorCode = result.getInt("error_code");
            switch (errorCode) {
                case 17:
                    // qps limit, can retry
                    throw new AipOverLimitException();
                    default:
                        throw new RuntimeException("调用错误");
            }
        }

        return StreamSupport.stream(result.getJSONArray("words_result").spliterator(), false)
                .map(jsonObj -> {
                    if (jsonObj instanceof JSONObject) {
                        return ((JSONObject) jsonObj).getString("words");
                    }
                    return "";
                }).collect(Collectors.joining(""));
    }
}

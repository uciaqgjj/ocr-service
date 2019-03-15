package com.daogu.data.ocrservice;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("baidu.aip")
@Data
@ToString
public class BaiduAipProperties {
    private String appKey;
    private String appId;
    private String secretKey;
}

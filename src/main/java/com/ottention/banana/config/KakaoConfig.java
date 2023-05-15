package com.ottention.banana.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "kakao")
public class KakaoConfig {
    private String restApiAppKey;
    private String getTokenUrl;
    private String getMemberInfoUrl;
    private String clientSecret;
    private String redirectUrl;
}

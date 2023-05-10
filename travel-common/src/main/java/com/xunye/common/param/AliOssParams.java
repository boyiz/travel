package com.xunye.common.param;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "ali.oss")
public class AliOssParams {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String role;
    private String sessionName;
    private Long duration;
    private String bucket;

}

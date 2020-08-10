package com.leyou.auth.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSconfig {

    @Bean
    public OSS ossClient(OSSProperties ossProperties){
        return new OSSClientBuilder().build(ossProperties.getEndpoint(),ossProperties.getAccessKeyId(),ossProperties.getAccessKeySecret());
    }
}

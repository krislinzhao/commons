package com.hikvision.commons.cache.properties;

import com.hikvision.commons.cache.util.StringUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "layering-cache.redis")
public class LayeringCacheRedisProperties {
    Integer database = 0;
    /**
     * 不为空表示集群版，示例
     * localhost:7379,localhost2:7379
     */
    String cluster = "";
    String host = "localhost";
    Integer port = 6379;
    String password = null;
    /**
     * 序列化方式:
     * com.hikvision.commons.cache.redis.serializer.JacksonRedisSerializer
     * com.hikvision.commons.cache.redis.serializer.JdkRedisSerializer
     */
    String serializer = "com.hikvision.commons.cache.redis.serializer.JacksonRedisSerializer";

    public String getPassword() {
        return StringUtils.isBlank(password) ? null : password;
    }
}
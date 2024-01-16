package com.hikvision.commons.cache.redis.client;

import lombok.Data;

@Data
public class RedisProperties {
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
}
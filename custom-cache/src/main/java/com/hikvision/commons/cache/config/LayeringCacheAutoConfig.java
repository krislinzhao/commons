package com.hikvision.commons.cache.config;

import com.hikvision.commons.cache.aspectj.LayeringAspect;
import com.hikvision.commons.cache.manager.CacheManager;
import com.hikvision.commons.cache.manager.LayeringCacheManager;
import com.hikvision.commons.cache.properties.LayeringCacheProperties;
import com.hikvision.commons.cache.properties.LayeringCacheRedisProperties;
import com.hikvision.commons.cache.redis.client.RedisClient;
import com.hikvision.commons.cache.redis.client.RedisProperties;
import com.hikvision.commons.cache.redis.serializer.AbstractRedisSerializer;
import com.hikvision.commons.cache.redis.serializer.StringRedisSerializer;
import com.hikvision.commons.cache.stats.extend.CacheStatsReportService;
import com.hikvision.commons.cache.stats.extend.DefaultCacheStatsReportServiceImpl;
import com.hikvision.commons.cache.util.GlobalConfig;
import com.hikvision.commons.cache.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * 多级缓存自动配置类
 *
 * @author xiaolyuh
 */
@Configuration
@EnableAspectJAutoProxy
@EnableConfigurationProperties({LayeringCacheProperties.class, LayeringCacheRedisProperties.class})
public class LayeringCacheAutoConfig {
    @Resource
    private Environment environment;

    @Value("${spring.application.name:}")
    private String applicationName;

    public static final String SPRING_REDIS_HOST = "spring.redis.host";
    /**
     * spring.redis.password=0mxiAxpm
     */
    public static final String SPRING_REDIS_PASSWORD = "spring.redis.password";
    /**
     * spring.redis.port=7019
     */
    public static final String SPRING_REDIS_PORT = "spring.redis.port";

    @Bean
    @ConditionalOnMissingBean(CacheManager.class)
    public CacheManager layeringCacheManager(RedisClient layeringCacheRedisClient, CacheStatsReportService cacheStatsReportService, LayeringCacheProperties layeringCacheProperties) {

        LayeringCacheManager layeringCacheManager = new LayeringCacheManager(layeringCacheRedisClient);
        // 默认开启统计功能
        layeringCacheManager.setStats(layeringCacheProperties.isStats());
        // 上报缓存统计信息
        layeringCacheManager.setCacheStatsReportService(cacheStatsReportService);
        // 设置缓存命名空间
        GlobalConfig.setNamespace(StringUtils.isBlank(layeringCacheProperties.getNamespace()) ? applicationName : layeringCacheProperties.getNamespace());
        return layeringCacheManager;
    }

    @Bean
    @ConditionalOnMissingBean(CacheStatsReportService.class)
    public CacheStatsReportService cacheStatsReportService() {
        return new DefaultCacheStatsReportServiceImpl();
    }

    @Bean
    public LayeringAspect layeringAspect() {
        return new LayeringAspect();
    }

    // @Bean
    // public RedisProperties redisProperties(LayeringCacheRedisProperties layeringCacheRedisProperties) {
    //     RedisProperties redisProperties = new RedisProperties();
    //     redisProperties.setDatabase(layeringCacheRedisProperties.getDatabase());
    //     redisProperties.setHost(layeringCacheRedisProperties.getHost());
    //     redisProperties.setCluster(layeringCacheRedisProperties.getCluster());
    //     redisProperties.setPassword(StringUtils.isBlank(layeringCacheRedisProperties.getPassword()) ? null : layeringCacheRedisProperties.getPassword());
    //     redisProperties.setPort(layeringCacheRedisProperties.getPort());
    //     redisProperties.setSerializer(layeringCacheRedisProperties.getSerializer());
    //     return redisProperties;
    // }

    /**
     * hikvison自动获取方式
     */
    @Bean
    public RedisProperties redisProperties() {
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setDatabase(0);
        redisProperties.setHost(environment.getProperty(SPRING_REDIS_HOST, String.class));
        redisProperties.setPassword(environment.getProperty(SPRING_REDIS_PASSWORD, String.class));
        redisProperties.setPort(environment.getProperty(SPRING_REDIS_PORT, Integer.class));
        return redisProperties;
    }

    @Bean
    @ConditionalOnMissingBean(RedisClient.class)
    public RedisClient layeringCacheRedisClient(RedisProperties redisProperties) throws Exception {
        AbstractRedisSerializer valueRedisSerializer = (AbstractRedisSerializer) Class.forName(redisProperties.getSerializer()).newInstance();
        StringRedisSerializer keyRedisSerializer = new StringRedisSerializer();

        RedisClient redisClient = RedisClient.getInstance(redisProperties);
        redisClient.setKeySerializer(keyRedisSerializer);
        redisClient.setValueSerializer(valueRedisSerializer);
        return redisClient;
    }

}

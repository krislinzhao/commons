package com.hikvision.commons.cache.manager;

import com.hikvision.commons.cache.core.Cache;
import com.hikvision.commons.cache.core.LayeringCache;
import com.hikvision.commons.cache.core.caffeine.CaffeineCache;
import com.hikvision.commons.cache.core.redis.RedisCache;
import com.hikvision.commons.cache.redis.client.RedisClient;
import com.hikvision.commons.cache.setting.LayeringCacheSetting;

/**
 * @author yuhao.wang
 */
public class LayeringCacheManager extends AbstractCacheManager {
    public LayeringCacheManager(RedisClient redisClient) {
        this.redisClient = redisClient;
        cacheManagers.add(this);
    }

    @Override
    protected Cache getMissingCache(String name, LayeringCacheSetting layeringCacheSetting) {
        // 创建一级缓存
        CaffeineCache caffeineCache = new CaffeineCache(name, layeringCacheSetting.getFirstCacheSetting(), getStats());
        // 创建二级缓存
        RedisCache redisCache = new RedisCache(name, redisClient, layeringCacheSetting.getSecondaryCacheSetting(), getStats());
        return new LayeringCache(redisClient, caffeineCache, redisCache, super.getStats(), layeringCacheSetting);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

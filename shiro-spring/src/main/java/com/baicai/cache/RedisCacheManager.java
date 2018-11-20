package com.baicai.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 基于redis自定义CacheManager实现类
 */
public class RedisCacheManager implements CacheManager {

    @Autowired
    private RedisCache redisCache;

    /** 获取缓存实例 */
    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return redisCache;
    }
}

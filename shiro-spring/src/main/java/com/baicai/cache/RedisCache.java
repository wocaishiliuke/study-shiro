package com.baicai.cache;

import com.baicai.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.util.Collection;
import java.util.Set;

/**
 * 自定义基于redis的cache
 *
 * Redis中的(授权)缓存：
 * k:(SHIRO_CACHE_PREFIX + new String(SerializationUtils.serialize(k))).getBytes()
 * v:SerializationUtils.serialize(v)
 * @param <K>
 * @param <V>
 */
@Component
public class RedisCache<K,V> implements Cache<K,V> {

    private static final String SHIRO_CACHE_PREFIX = "shiro-cahce:";

    @Autowired
    private JedisUtil jedisUtil;

    /** 取出 */
    @Override
    public V get(K k) throws CacheException {
        System.out.println("从redis获取授权数据...");
        byte[] value = jedisUtil.get(getKey(k));
        if (value != null)
            return (V) SerializationUtils.deserialize(value);
        return null;
    }

    /** 存入 */
    @Override
    public V put(K k, V v) throws CacheException {
        System.out.println("存入redis授权数据...");
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v);
        jedisUtil.set(key, value);
        jedisUtil.expire(key, 600);
        return v;
    }

    /** 删除 */
    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        jedisUtil.del(key);
        return value == null ? null : (V) SerializationUtils.deserialize(value);
    }

    /** 清除所有（授权）缓存 */
    @Override
    public void clear() throws CacheException {
        Set<byte[]> byteKeys = jedisUtil.keys(SHIRO_CACHE_PREFIX);
        if (!CollectionUtils.isEmpty(byteKeys)) {
            for (byte[] key : byteKeys) {
                jedisUtil.del(key);
            }
        }
    }

    /**（授权）缓存数量 */
    @Override
    public int size() {
        Set<byte[]> byteKeys = jedisUtil.keys(SHIRO_CACHE_PREFIX);
        return byteKeys == null ? 0 : byteKeys.size();
    }

    /** 获取redis中所有key对应的K实例 */
    @Override
    public Set<K> keys() {
        Set<byte[]> byteKeys = jedisUtil.keys(SHIRO_CACHE_PREFIX);
        Set<K> keys = null;
        if (!CollectionUtils.isEmpty(byteKeys)) {
            // byte[]和K之间的转换
            for (byte[] byteKey : byteKeys) {
                K k = (K) new String(byteKey).substring(SHIRO_CACHE_PREFIX.length()).getBytes();
                keys.add(k);
            }
        }
        return keys;
    }

    /** 获取redis中所有缓存实例 */
    @Override
    public Collection<V> values() {
        Set<byte[]> byteKeys = jedisUtil.keys(SHIRO_CACHE_PREFIX);
        Set<V> values = null;
        if (!CollectionUtils.isEmpty(byteKeys)) {
            for (byte[] key : byteKeys) {
                values.add((V) SerializationUtils.deserialize(jedisUtil.get(key)));
            }
        }
        return values;
    }

    /** 计算redis中cache的key */
    private byte[] getKey(K k) {
        /*if (k instanceof String) return (SHIRO_CACHE_PREFIX + k).getBytes();*/
        return (SHIRO_CACHE_PREFIX + new String(SerializationUtils.serialize(k))).getBytes();
    }
}
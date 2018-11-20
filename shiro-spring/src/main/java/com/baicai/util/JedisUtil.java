package com.baicai.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Jedis工具类
 */
@Component
public class JedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    @Autowired
    private JedisPool jedisPool;

    /** 获取连接 */
    private Jedis getResource() {
        return jedisPool.getResource();
    }

    /** 存入 */
    public void set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try {
            jedis.set(key, value);
        } catch (Exception e) {
            logger.error("存入redis出错");
            e.printStackTrace();
        } finally {
            // 确保连接关闭
            jedis.close();
        }
    }

    /** 取出 */
    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        byte[] value = null;
        try {
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error("取出redis出错");
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }

    /** 删除 */
    public void del(byte[] key) {
        Jedis jedis = getResource();
        try {
            jedis.del(key);
        } catch (Exception e) {
            logger.error("删除redis报错");
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

    /** 根据前缀，获取key */
    public Set<byte[]> keys(String prefix) {
        Jedis jedis = getResource();
        Set<byte[]> keys = null;
        try {
            keys = jedis.keys((prefix + "*").getBytes());
        } catch (Exception e) {
            logger.error("删除redis报错");
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return keys;
    }

    /** 过期时间 */
    public void expire(byte[] key, int second) {
        Jedis jedis = getResource();
        try {
            jedis.expire(key, second);
        } catch (Exception e) {
            logger.error("设置过期时间报错");
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }
}

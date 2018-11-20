package com.baicai.session;

import com.baicai.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * 自定义基于Redis的SessionDao
 *
 * Redis中的Session：
 * k:(SHIRO_SESSION_PREFIX + sessionId).getBytes()
 * v:SerializationUtils.serialize(session)
 */
public class RedisSessionDao extends AbstractSessionDAO {

    private static final String SHIRO_SESSION_PREFIX = "shiro-session:";

    @Autowired
    private JedisUtil jedisUtil;

    /**
     * 创建Session
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {
        // 为当前Session创建新的sessionId，并捆绑
        System.out.println("write session...");
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        saveSession(session);
        return sessionId;
    }

    /**
     * 获取Session
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("read session...");
        if (sessionId == null)
            return null;
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        return (Session) SerializationUtils.deserialize(value);
    }

    /**
     * 更新Session
     * @param session
     * @throws UnknownSessionException
     */
    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    /**
     * 删除Session
     * @param session
     */
    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null)
            return;
        byte[] key = getKey(session.getId().toString());
        jedisUtil.del(key);
    }

    /**
     * 获取所有Session
     * @return
     */
    @Override
    public Collection<Session> getActiveSessions() {
        // 根据前缀获取所有key
        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
        Collection<Session> sessions = null;
        if (CollectionUtils.isEmpty(keys))
            return sessions;
        for (byte[] key : keys) {
            sessions.add((Session) SerializationUtils.deserialize(jedisUtil.get(key)));
        }
        return sessions;
    }


    /** 获取redis中的key */
    private byte[] getKey(String originalKey) {
        return (SHIRO_SESSION_PREFIX + originalKey).getBytes();
    }

    /** 存入或更新redis中的session */
    private void saveSession(Session session) {
        // 将session存入redis
        byte[] key = getKey(session.getId().toString());
        byte[] value = SerializationUtils.serialize(session);
        jedisUtil.set(key, value);
        jedisUtil.expire(key, 600);//10min
    }
}

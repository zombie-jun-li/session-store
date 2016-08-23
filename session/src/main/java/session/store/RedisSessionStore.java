package session.store;


import redis.RedisClient;
import session.MapSession;
import util.Key;

import javax.inject.Inject;

/**
 * Created by li on 2016/6/13.
 */
public class RedisSessionStore extends SessionStore {

    @Inject
    private RedisClient redisClient;

    @Override
    public MapSession get(String sessionId) {
        MapSession mapSession = redisClient.get(createSessionKey(sessionId));
        if (null == mapSession) mapSession = new MapSession();
        mapSession.setMaxInactiveInterval(getMaxInactiveInterval());
        return mapSession;
    }

    @Override
    public void save(String sessionId, MapSession mapSession) {
        redisClient.setEx(createSessionKey(sessionId), mapSession, getMaxInactiveInterval());
    }

    @Override
    public void delete(String sessionId) {
        redisClient.del(createSessionKey(sessionId));
    }

    private Key<MapSession> createSessionKey(String sessionId) {
        return Key.key(sessionId, MapSession.class);
    }
}

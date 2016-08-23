package session.store;


import session.MapSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by li on 2016/6/13.
 */
public class LocalSessionStore extends SessionStore {

    private Map<String, MapSession> sessionStoreMap = new ConcurrentHashMap<>();

    @Override
    public MapSession get(String sessionId) {
        MapSession mapSession = sessionStoreMap.get(sessionId);
        if (null == mapSession) mapSession = new MapSession();
        if (isExpired(mapSession)) {
            sessionStoreMap.remove(sessionId);
            mapSession = new MapSession();
        }
        mapSession.setMaxInactiveInterval(getMaxInactiveInterval());
        return mapSession;
    }

    @Override
    public void save(String sessionId, MapSession mapSession) {
        sessionStoreMap.put(sessionId, mapSession);
    }

    @Override
    public void delete(String sessionId) {
        sessionStoreMap.remove(sessionId);
    }

    private Boolean isExpired(MapSession mapSession) {
        if (mapSession.getMaxInactiveInterval() < 0) {
            return false;
        }
        return System.currentTimeMillis() - TimeUnit.SECONDS.toMillis(mapSession.getMaxInactiveInterval()) >= mapSession.getLastAccessedTime();
    }
}

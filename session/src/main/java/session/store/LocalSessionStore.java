package session.store;


import session.MapSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by jun.
 */
public class LocalSessionStore extends SessionStore {

    private Map<String, MapSession> sessionStoreMap = new ConcurrentHashMap<>();

    @Override
    public MapSession get(String sessionId) {
        MapSession mapSession = sessionStoreMap.get(sessionId);
        if (null == mapSession) return null;
        if (isExpired(mapSession)) {
            sessionStoreMap.remove(sessionId);
            mapSession = new MapSession();
        }
        return mapSession;
    }

    @Override
    public void save(String sessionId, MapSession mapSession) {
        if (mapSession.getMaxInactiveInterval() != getMaxInactiveInterval()) {
            mapSession.setMaxInactiveInterval(getMaxInactiveInterval());
        }
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

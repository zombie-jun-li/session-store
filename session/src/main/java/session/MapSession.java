package session;

import session.store.SessionStore;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by li on 2016/6/18.
 */
public class MapSession {

    private long creationTime = System.currentTimeMillis();

    private long lastAccessedTime = creationTime;

    private int maxInactiveInterval = SessionStore.DEFAULT_MAX_INACTIVE_INTERVAL;

    private Map<String, Object> sessionData = new HashMap<>();

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public Map<String, Object> getSessionData() {
        return sessionData;
    }

    public void setSessionData(Map<String, Object> sessionData) {
        this.sessionData = sessionData;
    }

    public long getCreationTime() {
        return creationTime;
    }
}

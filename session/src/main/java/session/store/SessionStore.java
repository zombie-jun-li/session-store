package session.store;


import session.MapSession;

/**
 * Created by jun.
 */
public abstract class SessionStore {
    public static final int DEFAULT_MAX_INACTIVE_INTERVAL = 60 * 15;

    private int maxInactiveInterval = DEFAULT_MAX_INACTIVE_INTERVAL;

    public abstract MapSession get(String sessionId);

    public abstract void save(String sessionId, MapSession mapSession);

    public abstract void delete(String sessionId);

    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }
}

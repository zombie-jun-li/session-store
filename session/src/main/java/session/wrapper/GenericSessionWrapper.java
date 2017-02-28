package session.wrapper;


import session.MapSession;

import javax.servlet.ServletContext;

/**
 * Created by jun.
 */
public class GenericSessionWrapper extends SessionWrapper {

    private String sessionId;

    private MapSession mapSession;

    private boolean changed;

    private boolean valid = true;

    public GenericSessionWrapper(String sessionId, MapSession mapSession, ServletContext servletContext) {
        super(servletContext);
        this.sessionId = sessionId;
        this.mapSession = mapSession;
    }

    @Override
    public long getCreationTime() {
        return mapSession.getCreationTime();
    }

    @Override
    public String getId() {
        return sessionId;
    }

    @Override
    public long getLastAccessedTime() {
        checkState();
        return mapSession.getLastAccessedTime();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        checkState();
        changed = true;
        mapSession.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        checkState();
        return mapSession.getMaxInactiveInterval();
    }

    @Override
    public Object getAttribute(String name) {
        checkState();
        return mapSession.getSessionData().get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        checkState();
        changed = true;
        mapSession.getSessionData().put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        checkState();
        changed = true;
        mapSession.getSessionData().remove(name);
    }

    @Override
    public void invalidate() {
        valid = false;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        changed = true;
        mapSession.setLastAccessedTime(lastAccessedTime);
    }

    public boolean isChanged() {
        return changed;
    }

    public boolean isValid() {
        return valid;
    }

    public void checkState() {
        if (!isValid()) {
            throw new IllegalStateException("Session invalid.");
        }
    }

    public MapSession getMapSession() {
        return mapSession;
    }
}

package session.wrapper;


import session.MapSession;

import javax.servlet.ServletContext;

/**
 * Created by li on 2016/6/10.
 */
public class GenericSessionWrapper extends SessionWrapper {

    private String sessionId;

    private MapSession mapSession;

    private Boolean changed = false;

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
        return mapSession.getLastAccessedTime();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        changed = true;
        mapSession.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return mapSession.getMaxInactiveInterval();
    }

    @Override
    public Object getAttribute(String name) {
        return mapSession.getSessionData().get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        changed = true;
        mapSession.getSessionData().put(name, value);
    }

    @Override
    public void removeAttribute(String name) {
        changed = true;
        mapSession.getSessionData().remove(name);
    }

    @Override
    public void invalidate() {
        // todo
        mapSession.getSessionData().clear();
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        changed = true;
        mapSession.setLastAccessedTime(lastAccessedTime);
    }

    public boolean isChanged() {
        return changed;
    }

    public MapSession getMapSession() {
        return mapSession;
    }
}

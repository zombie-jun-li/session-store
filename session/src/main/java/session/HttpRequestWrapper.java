package session;



import cookie.CookieBuilder;
import session.store.SessionStore;
import session.wrapper.GenericSessionWrapper;
import util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by jun.
 */
public class HttpRequestWrapper extends HttpServletRequestWrapper {

    private static final String SESSION_COOKIE_NAME = "session_id";

    private boolean saved;

    private SessionStore sessionStore;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private GenericSessionWrapper sessionWrapper;

    public HttpRequestWrapper(SessionStore sessionStore, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(httpServletRequest);
        this.sessionStore = sessionStore;
        this.request = httpServletRequest;
        this.response = httpServletResponse;
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (null != sessionWrapper && !sessionWrapper.isValid()) {
            throw new IllegalStateException("Session invalid.");
        }
        if (null == sessionWrapper && create) {
            String sessionId = getAndSetCookieSessionId(false);
            MapSession mapSession = sessionStore.get(sessionId);
            sessionWrapper = new GenericSessionWrapper(sessionId, null == mapSession ? new MapSession() : mapSession, request.getServletContext());
        }
        if (null != sessionWrapper) sessionWrapper.setLastAccessedTime(System.currentTimeMillis());
        return sessionWrapper;
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    public void saveSession() {
        if (saved) return;
        if (null == sessionWrapper) return;
        String sessionId = sessionWrapper.getId();
        if (!sessionWrapper.isValid()) {
            sessionStore.delete(sessionId);
            delCookieSessionId();
        } else if (sessionWrapper.isChanged()) {
            saveSession(sessionId, sessionWrapper.getMapSession());
        }
        saved = true;
    }

    @Override
    public String changeSessionId() {
        String oldSessionId = getAndSetCookieSessionId(false);
        MapSession mapSession = sessionStore.get(oldSessionId);
        if (mapSession == null) {
            throw new IllegalStateException("There is no session associated with this request.");
        }
        sessionStore.delete(oldSessionId);
        String newSessionId = getAndSetCookieSessionId(true);
        saveSession(newSessionId, mapSession);
        return newSessionId;
    }

    private String getAndSetCookieSessionId(boolean overwrite) {
        Cookie sessionCookie = WebUtils.getCookie(request, SESSION_COOKIE_NAME);
        if (null == sessionCookie || overwrite) {
            String sessionId = generateSessionId();
            sessionCookie = CookieBuilder.build(SESSION_COOKIE_NAME, sessionId).getCookie();
            response.addCookie(sessionCookie);
        }
        return sessionCookie.getValue();
    }

    private void delCookieSessionId() {
        Cookie sessionCookie = CookieBuilder.build(SESSION_COOKIE_NAME, null).path("/").maxAge(0).getCookie();
        sessionCookie.setValue(null);
        sessionCookie.setMaxAge(0);
        response.addCookie(sessionCookie);
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    private void saveSession(String sessionId, MapSession mapSession) {
        sessionStore.save(sessionId, mapSession);
    }
}

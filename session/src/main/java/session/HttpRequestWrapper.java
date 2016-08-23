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
 * Created by li on 2016/6/13.
 */
public class HttpRequestWrapper extends HttpServletRequestWrapper {

    private static final String SESSION_COOKIE_NAME = "session_id";

    private SessionStore sessionStore;

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;

    private GenericSessionWrapper sessionWrapper;

    public HttpRequestWrapper(SessionStore sessionStore, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        super(httpServletRequest);
        this.sessionStore = sessionStore;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (null == sessionWrapper && create) {
            String sessionId = getAndSetCookieSessionId(false);
            MapSession mapSession = sessionStore.get(sessionId);
            sessionWrapper = new GenericSessionWrapper(sessionId, mapSession, httpServletRequest.getServletContext());
        }
        if (null != sessionWrapper) sessionWrapper.setLastAccessedTime(System.currentTimeMillis());
        return sessionWrapper;
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    public void saveSession() {
        if (null != sessionWrapper && sessionWrapper.isChanged()) {
            sessionStore.save(sessionWrapper.getId(), sessionWrapper.getMapSession());
        }
    }

    @Override
    public String changeSessionId() {
        if (null == sessionWrapper) {
            String oldSessionId = getAndSetCookieSessionId(false);
            MapSession mapSession = sessionStore.get(oldSessionId);
            if (mapSession == null) {
                throw new IllegalStateException("There is no session associated with this request.");
            }
            sessionWrapper = new GenericSessionWrapper(oldSessionId, mapSession, httpServletRequest.getServletContext());
            sessionStore.delete(oldSessionId);
        }
        String sessionId = getAndSetCookieSessionId(true);
        sessionStore.save(sessionId, sessionWrapper.getMapSession());
        return sessionId;
    }

    private String getAndSetCookieSessionId(boolean overwrite) {
        Cookie sessionCookie = WebUtils.getCookie(httpServletRequest, SESSION_COOKIE_NAME);
        if (null == sessionCookie || overwrite) {
            String sessionId = generateSessionId();
            sessionCookie = CookieBuilder.build(SESSION_COOKIE_NAME, sessionId).getCookie();
            httpServletResponse.addCookie(sessionCookie);
        }
        return sessionCookie.getValue();
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}

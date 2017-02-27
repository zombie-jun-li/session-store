import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import session.MapSession;
import session.SessionFilter;
import session.store.LocalSessionStore;
import session.store.SessionStore;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by jun.
 */
public class SessionFilterTest {

    private SessionFilter sessionFilter;

    private RequestWrapper requestWrapper;

    private ResponseWrapper responseWrapper;

    private SessionStore sessionStore;

    private String sessionAttrName = "object";

    private String sessionCookieName = "session_id";

    @Before
    public void init() {
        sessionStore = new LocalSessionStore();
        sessionFilter = new SessionFilter();
        sessionFilter.setSessionStore(sessionStore);
        requestWrapper = new RequestWrapper(mock(HttpServletRequest.class));
        responseWrapper = new ResponseWrapper(mock(HttpServletResponse.class));
    }

    @After
    public void clear() {
        requestWrapper.clear();
        responseWrapper.clear();
        sessionStore = null;
    }

    @Test
    public void testSetAttribute() throws IOException, ServletException {
        setSessionDataAndGetCookie();
    }

    @Test
    public void testChangeSessionId() throws IOException, ServletException {
        final AtomicInteger requestCount = new AtomicInteger();
        Cookie oldSessionCookie = setSessionDataAndGetCookie();
        clear();
        requestWrapper.addCookie(oldSessionCookie);
        sessionFilter.doFilter(requestWrapper, responseWrapper, (request, response) -> {
            requestCount.getAndIncrement();
            ((HttpServletRequest) request).changeSessionId();
        });
        List<Cookie> cookies = responseWrapper.getCookie(sessionCookieName);
        assertTrue(cookies.size() == 1);
        Cookie newSessionCookie = cookies.get(0);
        assertNotEquals(oldSessionCookie.getValue(), newSessionCookie.getValue());
        assertEquals(1, requestCount.get());
    }

    @Test
    public void testDeleteSessionData() throws IOException, ServletException {
        final AtomicInteger requestCount = new AtomicInteger();
        Cookie sessionCookie = setSessionDataAndGetCookie();
        requestWrapper.addCookie(sessionCookie);
        sessionFilter.doFilter(requestWrapper, responseWrapper, (request, response) -> {
            requestCount.getAndIncrement();
            ((HttpServletRequest) request).getSession().removeAttribute(sessionAttrName);
        });

        MapSession mapSession = sessionStore.get(sessionCookie.getValue());
        assertNull(mapSession.getSessionData().get(sessionAttrName));
        assertEquals(1, requestCount.get());
    }

    private Cookie setSessionDataAndGetCookie() throws IOException, ServletException {
        final AtomicInteger requestCount = new AtomicInteger();
        Object object = new Object();
        sessionFilter.doFilter(requestWrapper, responseWrapper, (request, response) -> {
            requestCount.getAndIncrement();
            ((HttpServletRequest) request).getSession().setAttribute(sessionAttrName, object);
        });
        List<Cookie> cookies = responseWrapper.getCookie(sessionCookieName);
        assertTrue(cookies.size() == 1);
        Cookie sessionCookie = cookies.get(0);
        MapSession mapSession = sessionStore.get(sessionCookie.getValue());

        assertEquals(object, mapSession.getSessionData().get(sessionAttrName));
        assertEquals(1, requestCount.get());
        return sessionCookie;
    }

    private static class RequestWrapper extends HttpServletRequestWrapper {

        private List<Cookie> cookieList = new ArrayList<>();

        public RequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public Cookie[] getCookies() {
            if (null == super.getCookies() && cookieList.size() == 0) {
                return null;
            }
            if (null != super.getCookies()) {
                cookieList.addAll(Arrays.asList(super.getCookies()));
            }
            return cookieList.toArray(new Cookie[cookieList.size()]);
        }

        public void addCookie(Cookie cookie) {
            cookieList.add(cookie);
        }

        public void clear() {
            cookieList.clear();
        }
    }


    private static class ResponseWrapper extends HttpServletResponseWrapper {
        private List<Cookie> cookieList = new ArrayList<>();

        public ResponseWrapper(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void addCookie(Cookie cookie) {
            super.addCookie(cookie);
            cookieList.add(cookie);
        }

        public List<Cookie> getCookie(String cookieName) {
            return cookieList.stream()
                    .filter(cookie -> cookieName.equals(cookie.getName()))
                    .collect(Collectors.toList());
        }

        public void clear() {
            cookieList.clear();
        }
    }

}

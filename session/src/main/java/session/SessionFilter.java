package session;


import session.store.LocalSessionStore;
import session.store.SessionStore;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jun.
 */
public class SessionFilter implements Filter {

    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = SessionFilter.class.getName() + ".SESSION_ID_INITIALIZED";

    private SessionStore sessionStore = new LocalSessionStore();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing will be init
    }

    @Override
    public void destroy() {
        // nothing will be destroy
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (initialized(httpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpRequestWrapper servletRequest = new HttpRequestWrapper(sessionStore, httpServletRequest, httpServletResponse);
        HttpResponseWrapper servletResponse = new HttpResponseWrapper(httpServletResponse, servletRequest);
        try {
            servletRequest.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
            chain.doFilter(servletRequest, servletResponse);
        } finally {
            servletRequest.saveSession();
        }
    }

    private boolean initialized(HttpServletRequest request) {
        return Boolean.TRUE.equals(request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED));
    }

    public void setSessionStore(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }
}

package session.wrapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * Created by li on 2016/6/10.
 */
public abstract class SessionWrapper implements HttpSession {

    private ServletContext servletContext;

    public SessionWrapper(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        throw new UnsupportedOperationException("deprecated");
    }

    @Override
    public Object getValue(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getValueNames() {
        throw new UnsupportedOperationException("use getAttributeNames");
    }

    @Override
    public void putValue(String name, Object value) {
        throw new UnsupportedOperationException("deprecated");
    }

    @Override
    public void removeValue(String name) {
        throw new UnsupportedOperationException("deprecated");
    }

    @Override
    public boolean isNew() {
        return false;
    }
}

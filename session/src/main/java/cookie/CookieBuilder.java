package cookie;

import javax.servlet.http.Cookie;

/**
 * Created by jun.
 */
public final class CookieBuilder {
    private Cookie cookie;

    private CookieBuilder(Cookie cookie) {
        this.cookie = cookie;
    }

    public static CookieBuilder build(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        return new CookieBuilder(cookie);
    }

    public CookieBuilder domain(String domain) {
        cookie.setDomain(domain);
        return this;
    }

    public CookieBuilder maxAge(int maxAge) {
        cookie.setMaxAge(maxAge);
        return this;
    }

    public CookieBuilder path(String path) {
        cookie.setPath(path);
        return this;
    }

    public Cookie getCookie() {
        return cookie;
    }
}

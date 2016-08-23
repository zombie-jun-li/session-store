package store;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractDispatcherServletInitializer;
import session.SessionFilter;
import session.store.RedisSessionStore;
import session.store.SessionStore;

import javax.servlet.Filter;

/**
 * Created by li on 2016/8/22.
 */
@Configuration
@ComponentScan(basePackageClasses = WebConfig.class)
public class WebConfig extends AbstractDispatcherServletInitializer {
    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(this.getClass());
        return context;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected WebApplicationContext createRootApplicationContext() {
       return null;
    }

    protected Filter[] getServletFilters() {
        return new Filter[]{new DelegatingFilterProxy("sessionFilter")};
    }

    @Bean
    public SessionFilter sessionFilter() {
        SessionFilter sessionFilter = new SessionFilter();
        sessionFilter.setSessionStore(sessionStore());
        return sessionFilter;
    }

    @Bean
    public SessionStore sessionStore() {
        SessionStore sessionStore = new RedisSessionStore();
        sessionStore.setMaxInactiveInterval(10 * 60);
        return sessionStore;
    }

}

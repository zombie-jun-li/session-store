# Session-store
Distrbuted session management with redis

# Simple usage
# Register a filter proxy
	new DelegatingFilterProxy("sessionFilter")

# Register spring bean
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
	
# License
Copyright 2016 zombie.jun.li@aliyun.com

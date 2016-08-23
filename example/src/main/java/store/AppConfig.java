package store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import redis.RedisClient;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by li on 2016/8/23.
 */
@Configuration
@PropertySource("classpath:redis.properties")
public class AppConfig {
    @Autowired
    Environment env;

    @Bean
    public RedisClient redisClient() {
        return new RedisClient();
    }

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(env.getProperty("redis.max.idle", int.class, 8));
        config.setMaxTotal(env.getProperty("redis.max.total", int.class, 8));
        config.setMaxWaitMillis(env.getProperty("redis.max.wait.millis", long.class, -1L));
        return new JedisPool(config, env.getProperty("redis.host"), env.getProperty("redis.port", int.class, 5432));
    }
}

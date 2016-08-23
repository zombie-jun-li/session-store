package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.command.Command;
import redis.command.DelCommand;
import redis.command.GetCommand;
import redis.command.SetCommand;
import redis.command.SetExCommand;
import util.Key;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * Created by li on 2016/8/3.
 */
public class RedisClient {
    @Inject
    JedisPool jedisPool;

    public <T> T execute(Command<T> command) {
        Jedis jedis = jedisPool.getResource();
        T val;
        try {
            val = command.execute(jedis);
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
        return val;
    }

    public <T> T get(Key<T> key) {
        return execute(new GetCommand<>(key));
    }

    public <T> String set(Key<T> key, T value) {
        return execute(new SetCommand<>(key, value));
    }

    public <T> String setEx(Key<T> key, T value, int seconds) {
        return execute(new SetExCommand<>(key, value, seconds));
    }

    public <T> Long del(Key<T> key) {
        return execute(new DelCommand<>(key));
    }

    @PreDestroy
    public void destroy() {
        if (null != jedisPool) {
            jedisPool.destroy();
        }
    }
}

package redis.command;

import redis.clients.jedis.Jedis;

/**
 * Created by jun.
 */
public interface Command<T> {
    T execute(Jedis jedis);
}

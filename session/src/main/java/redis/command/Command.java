package redis.command;

import redis.clients.jedis.Jedis;

/**
 * Created by li on 2016/8/4.
 */
public interface Command<T> {
    T execute(Jedis jedis);
}

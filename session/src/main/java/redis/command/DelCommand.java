package redis.command;

import redis.clients.jedis.Jedis;
import util.Key;

/**
 * Created by jun.
 */
public class DelCommand<T> implements Command<Long> {

    private Key<T> key;

    public DelCommand(Key<T> key) {
        this.key = key;
    }

    @Override
    public Long execute(Jedis jedis) {
        return jedis.del(key.name());
    }
}

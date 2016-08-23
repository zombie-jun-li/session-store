package redis.command;

import redis.clients.jedis.Jedis;
import util.Key;

/**
 * Created by li on 2016/8/6.
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

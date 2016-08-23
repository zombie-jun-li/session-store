package redis.command;

import redis.clients.jedis.Jedis;
import util.Key;
import util.TypeConverter;

/**
 * Created by li on 2016/8/4.
 */
public class GetCommand<T> implements Command<T> {
    Key<T> key;

    public GetCommand(Key<T> key) {
        this.key = key;
    }

    @Override
    public T execute(Jedis jedis) {
        return TypeConverter.fromString(jedis.get(key.name()), key.targetClass());
    }
}

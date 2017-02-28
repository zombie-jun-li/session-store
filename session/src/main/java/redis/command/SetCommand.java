package redis.command;

import exception.NullException;
import redis.clients.jedis.Jedis;
import util.JSON;
import util.Key;

/**
 * Created by jun.
 */
public class SetCommand<T> implements Command<String> {
    Key<T> key;

    T value;

    public SetCommand(Key<T> key, T value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String execute(Jedis jedis) {
        if (null == value) throw new NullException("Value can't be null");
        String strValue = key.targetClass().isPrimitive() ? String.valueOf(value) : JSON.toJson(value);
        return jedis.set(key.name(), strValue);
    }
}

package redis.command;

import exception.NullException;
import redis.clients.jedis.Jedis;
import util.JSON;
import util.Key;

/**
 * Created by li on 2016/8/22.
 */
public class SetExCommand<T> implements Command<String> {
    Key<T> key;

    T value;

    private int seconds;

    public SetExCommand(Key<T> key, T value, int seconds) {
        this.key = key;
        this.value = value;
        this.seconds = seconds;
    }

    @Override
    public String execute(Jedis jedis) {
        if (null == value) throw new NullException("Value can't be null");
        String strValue = key.targetClass().isPrimitive() ? String.valueOf(value) : JSON.toJson(value);
        return jedis.setex(key.name(), seconds, strValue);
    }
}

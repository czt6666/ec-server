package com.bistu.common.config.cache;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Function;

public interface CacheDao {
    /**
     * glob模糊匹配
     * @param pattern 想要检查的key的格式，如"xReader:*"表示所有以xReader:开头的key.
     */
    int getKeyNum(String pattern);

    /**
     * glob模糊匹配
     * @param pattern 想要检查的key的格式，如"xReader:*"表示所有以xReader:开头的key.
     * @return
     */
    Collection<String> getKeys(String pattern);
    /**
     *
     * @param key
     * @param timeout：过期时间，可以为null(不过期)。LocalCache不支持设置时间，缓存时间与xReader对象保持一致。
     * @param value 单个key-value.
     */
    void set(String key , Object value, Duration timeout);
    /**
     * 获取key对应的value,需要自己转换类型。
     * 不存在时返回Null.
     * @param
     * @param key
     * @return Object
     */
    String get(String key);

    boolean delete(String key);

    int cleanUp(String pattern, int maxNum, int eliminateTime, Function<String,Boolean> process);

    boolean hasKey(String key);

    int LRUClean(String pattern, int maxNum, int deleteNum, Function<String, Boolean> process);
}

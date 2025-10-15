package com.bistu.system.admin.dao.redis;

import com.alibaba.fastjson.JSON;
import com.bistu.common.cacheobject.UserCacheObject;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserCacheDao {
    /**
     * user:用户编号 ：通过静态变量，声明 KEY 的前缀，并且使用冒号作为间隔
     */
    private static final String KEY_PATTERN = "user:%d";

    /**
     * 通过 @Resource 注入指定名字的 RedisTemplate 对应的 Operations 对象，这样明确每个 KEY 的类型
     */
    @Resource(name = "redisTemplate")
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private ValueOperations<String, String> operations;

    /**
     * 声明 KEY_PATTERN 对应的 KEY 拼接方法，避免散落在每个方法中。
     * @param id
     * @return
     */
    private static String buildKey(Integer id) {
        return String.format(KEY_PATTERN, id);
    }

    /**
     * 获取User缓存对象
     * @param id
     * @return
     */
    public UserCacheObject get(Integer id) {
        String key = buildKey(id);
        String value = operations.get(key);
        return JSON.parseObject(value, UserCacheObject.class);
    }


    /**
     * 将User缓存对象<key,value>存入redis
     * @param id
     * @param object
     */
    public void set(Integer id, UserCacheObject object) {
        String key = buildKey(id);
        String value = JSON.toJSONString(object);
        operations.set(key, value);
    }
}

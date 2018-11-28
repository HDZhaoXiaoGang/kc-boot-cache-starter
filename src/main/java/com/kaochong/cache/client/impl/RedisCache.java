package com.kaochong.cache.client.impl;

import com.kaochong.cache.client.InterfaceCache;
import com.kaochong.cache.enums.CacheType;
import com.kaochong.cache.annotation.KCCacheEvict;
import com.kaochong.cache.annotation.KCCachePut;
import com.kaochong.cache.annotation.KCCacheable;
import com.kaochong.cache.utils.SPELUtil;
import com.kaochong.cache.utils.SerializableUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisCache implements InterfaceCache {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void set(KCCacheable kcCacheable, String[] parameterNames, Object[] args, Object data) throws IOException {
        String cacheNames = kcCacheable.cacheNames();
        String key = kcCacheable.key();
        long redisExpire = kcCacheable.redisExpire();
        String unless = kcCacheable.unless();

        boolean spelBoolean = SPELUtil.getSPELBoolean(data, unless);
        if (!spelBoolean) {
            return;
        }

        String keys = cacheNames + ":" + SPELUtil.getSPELString(parameterNames, args, key);
        stringRedisTemplate.opsForValue().set(keys, SerializableUtil.writeObject(data));
        stringRedisTemplate.expire(keys, redisExpire, TimeUnit.MINUTES);
    }

    public Object get(KCCacheable kcCacheable, String[] parameterNames, Object[] args, Class returnType) throws IOException, ClassNotFoundException {
        String cacheNames = kcCacheable.cacheNames();
        String key = kcCacheable.key();
        long redisExpire = kcCacheable.redisExpire();
        boolean redisUpExpire = kcCacheable.redisUpExpire();

        String keys = cacheNames + ":" + SPELUtil.getSPELString(parameterNames, args, key);
        String s = stringRedisTemplate.opsForValue().get(keys);
        if (redisUpExpire) {
            stringRedisTemplate.expire(keys, redisExpire, TimeUnit.MINUTES);
        }
        return SerializableUtil.readObject(s);
    }

    public void set(KCCachePut kcCachePut, String[] parameterNames, Object[] args, Object data) throws IOException {
        CacheType cacheType = kcCachePut.cacheType();
        String cacheNames = kcCachePut.cacheNames();
        String key = kcCachePut.key();
        long redisExpire = kcCachePut.redisExpire();
        String unless = kcCachePut.unless();

        boolean spelBoolean = SPELUtil.getSPELBoolean(data, unless);
        if (!spelBoolean) {
            return;
        }

        String keys = cacheNames + ":" + SPELUtil.getSPELString(parameterNames, args, key);
        stringRedisTemplate.opsForValue().set(keys, SerializableUtil.writeObject(data));
        stringRedisTemplate.expire(keys, redisExpire, TimeUnit.MINUTES);
    }

    public void del(KCCacheEvict kcCacheEvict, String[] parameterNames, Object[] args, Object data) {
        CacheType cacheType = kcCacheEvict.cacheType();
        String[] cacheNames = kcCacheEvict.cacheNames();
        String key = kcCacheEvict.key();
        boolean allEntries = kcCacheEvict.allEntries();
        String condition = kcCacheEvict.condition();

        boolean spelBoolean = SPELUtil.getSPELBoolean(parameterNames, args, condition);
        if (spelBoolean) {
            for (String cacheName : cacheNames) {
                if (allEntries) {
                    Set<String> keyss = stringRedisTemplate.keys(cacheName + "*");
                    Long delete = stringRedisTemplate.delete(keyss);
                } else {
                    String keys = cacheName + ":" + SPELUtil.getSPELString(parameterNames, args, key);
                    Boolean delete = stringRedisTemplate.delete(keys);
                }
            }
        }
    }

}

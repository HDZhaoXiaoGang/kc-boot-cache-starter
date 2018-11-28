package com.kaochong.cache.adapter;

import com.kaochong.cache.client.InterfaceCache;
import com.kaochong.cache.enums.CacheType;
import com.kaochong.cache.annotation.KCCacheEvict;
import com.kaochong.cache.annotation.KCCachePut;
import com.kaochong.cache.annotation.KCCacheable;
import com.kaochong.cache.client.impl.EhcacheCache;
import com.kaochong.cache.client.impl.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheAdapter {
    @Autowired
    InterfaceCache redisCache;
    @Autowired
    InterfaceCache ehcacheCache;


    public void set(KCCacheable kcCacheable, String[] parameterNames, Object[] args, Object data) {
        try {
            CacheType cacheType = kcCacheable.cacheType();
            if (CacheType.REDIS == cacheType) {
                redisCache.set(kcCacheable, parameterNames, args, data);
            } else if (CacheType.EHCACHE == cacheType) {
                ehcacheCache.set(kcCacheable, parameterNames, args, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object get(KCCacheable kcCacheable, String[] parameterNames, Object[] args, Class returnType) {
        try {
            CacheType cacheType = kcCacheable.cacheType();
            if (CacheType.REDIS == cacheType) {
                return redisCache.get(kcCacheable, parameterNames, args, returnType);
            } else if (CacheType.EHCACHE == cacheType) {
                return ehcacheCache.get(kcCacheable, parameterNames, args, returnType);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void set(KCCachePut kcCachePut, String[] parameterNames, Object[] args, Object data) {
        try {
            CacheType cacheType = kcCachePut.cacheType();
            if (CacheType.REDIS == cacheType) {
                redisCache.set(kcCachePut, parameterNames, args, data);
            } else if (CacheType.EHCACHE == cacheType) {
                ehcacheCache.set(kcCachePut, parameterNames, args, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void del(KCCacheEvict kcCacheEvict, String[] parameterNames, Object[] args, Object data) {
        try {
            CacheType cacheType = kcCacheEvict.cacheType();

            if (CacheType.REDIS == cacheType) {
                redisCache.del(kcCacheEvict, parameterNames, args, data);
            } else if (CacheType.EHCACHE == cacheType) {
                ehcacheCache.del(kcCacheEvict, parameterNames, args, data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

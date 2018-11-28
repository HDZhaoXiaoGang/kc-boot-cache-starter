package com.kaochong.cache.client.impl;

import com.kaochong.cache.client.InterfaceCache;
import com.kaochong.cache.enums.CacheType;
import com.kaochong.cache.annotation.KCCacheEvict;
import com.kaochong.cache.annotation.KCCachePut;
import com.kaochong.cache.annotation.KCCacheable;
import com.kaochong.cache.utils.SPELUtil;
import com.kaochong.cache.utils.SerializableUtil;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class EhcacheCache implements InterfaceCache {

    CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

    public void set(KCCacheable kcCacheable, String[] parameterNames, Object[] args, Object data) throws IOException {
        String cacheNames = kcCacheable.cacheNames();
        String key = kcCacheable.key();
        long ehcacheSize = kcCacheable.ehcacheSize();
        long ehcacheIdleExpire = kcCacheable.ehcacheIdleExpire();
        long ehcacheLiveExpire = kcCacheable.ehcacheLiveExpire();
        String unless = kcCacheable.unless();

        boolean spelBoolean = SPELUtil.getSPELBoolean(data, unless);
        if (!spelBoolean) {
            return;
        }

        String keys = cacheNames + ":" + SPELUtil.getSPELString(parameterNames, args, key);

        Cache<String, String> cache = cacheManager.getCache(cacheNames, String.class, String.class);

        if (cache == null) {
            synchronized (EhcacheCache.class) {
                cache = cacheManager.getCache(cacheNames, String.class, String.class);
                if (cache == null) {
                    ResourcePoolsBuilder heap = ResourcePoolsBuilder.heap(ehcacheSize);
                    CacheConfigurationBuilder<String, String> ccb = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, heap);
                    if (ehcacheIdleExpire > 0) {
                        ccb = ccb.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(ehcacheIdleExpire)));
                    }
                    if (ehcacheLiveExpire > 0) {
                        ccb = ccb.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(ehcacheLiveExpire)));
                    }
                    cache = cacheManager.createCache(cacheNames, ccb);
                }
            }
        }

        cache.put(keys, SerializableUtil.writeObject(data));
    }

    public Object get(KCCacheable kcCacheable, String[] parameterNames, Object[] args, Class returnType) throws IOException, ClassNotFoundException {
        String cacheNames = kcCacheable.cacheNames();
        String key = kcCacheable.key();
        long redisExpire = kcCacheable.redisExpire();

        String keys = cacheNames + ":" + SPELUtil.getSPELString(parameterNames, args, key);

        Cache<String, String> cache = cacheManager.getCache(cacheNames, String.class, String.class);
        if (cache == null) {
            return null;
        }
        String s = cache.get(keys);
        return SerializableUtil.readObject(s);
    }

    public void set(KCCachePut kcCachePut, String[] parameterNames, Object[] args, Object data) throws IOException {
        CacheType cacheType = kcCachePut.cacheType();
        String cacheNames = kcCachePut.cacheNames();
        String key = kcCachePut.key();
        long ehcacheSize = kcCachePut.ehcacheSize();
        long ehcacheIdleExpire = kcCachePut.ehcacheIdleExpire();
        long ehcacheLiveExpire = kcCachePut.ehcacheLiveExpire();
        String unless = kcCachePut.unless();

        boolean spelBoolean = SPELUtil.getSPELBoolean(data, unless);
        if (!spelBoolean) {
            return;
        }

        String keys = cacheNames + ":" + SPELUtil.getSPELString(parameterNames, args, key);
        Cache<String, String> cache = cacheManager.getCache(cacheNames, String.class, String.class);
        if (cache == null) {
            synchronized (EhcacheCache.class) {
                cache = cacheManager.getCache(cacheNames, String.class, String.class);
                if (cache == null) {
                    ResourcePoolsBuilder heap = ResourcePoolsBuilder.heap(ehcacheSize);
                    CacheConfigurationBuilder<String, String> ccb = CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, heap);
                    if (ehcacheIdleExpire > 0) {
                        ccb = ccb.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(ehcacheIdleExpire)));
                    }
                    if (ehcacheLiveExpire > 0) {
                        ccb = ccb.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(ehcacheLiveExpire)));
                    }
                    cache = cacheManager.createCache(cacheNames, ccb);
                }
            }
        }

        cache.put(keys, SerializableUtil.writeObject(data));
    }

    public void del(KCCacheEvict kcCacheEvict, String[] parameterNames, Object[] args, Object data) {
        CacheType cacheType = kcCacheEvict.cacheType();
        String[] cacheNames = kcCacheEvict.cacheNames();
        String key = kcCacheEvict.key();
        boolean allEntries = kcCacheEvict.allEntries();
        for (String cacheName : cacheNames) {
            String keys = cacheName + ":" + SPELUtil.getSPELString(parameterNames, args, key);

            Cache<String, String> cache = cacheManager.getCache(cacheName, String.class, String.class);
            if (cache != null) {
                if (allEntries) {
                    cacheManager.removeCache(cacheName);
                } else {
                    cache.remove(keys);
                }
            }
        }
    }
}

package com.kaochong;


import com.kaochong.cache.adapter.CacheAdapter;
import com.kaochong.cache.client.impl.EhcacheCache;
import com.kaochong.cache.aspect.KCCacheAspect;
import com.kaochong.cache.client.impl.RedisCache;
import org.springframework.context.annotation.*;


@Configuration
public class CacheAutoConfiguration {


    @Bean
    public EhcacheCache ehcacheCache() {
        return new EhcacheCache();
    }

    @Bean
    public RedisCache redisCache() {
        return new RedisCache();
    }


    @Bean
    public CacheAdapter cacheAdapter() {
        return new CacheAdapter();
    }


    @Bean
    public KCCacheAspect kCCacheAspect() {
        return new KCCacheAspect();
    }

}

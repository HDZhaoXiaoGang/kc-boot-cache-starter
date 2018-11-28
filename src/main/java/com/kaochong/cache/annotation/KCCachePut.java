package com.kaochong.cache.annotation;

import com.kaochong.cache.enums.CacheType;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface KCCachePut {

    CacheType cacheType() default CacheType.REDIS;

    String cacheNames() default "cache";

    String key() default "";

    String condition() default "";

    String unless() default "";

    long redisExpire() default 60L;

    long ehcacheSize() default 100L;

    long ehcacheIdleExpire() default 0L;

    long ehcacheLiveExpire() default 0L;
}


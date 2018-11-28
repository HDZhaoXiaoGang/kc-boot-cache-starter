package com.kaochong.cache.annotation;

import com.kaochong.cache.enums.CacheType;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface KCCacheEvict {

    CacheType cacheType() default CacheType.REDIS;

    String[] cacheNames() default {};

    String key() default "";

    String condition() default "";

    String unless() default "";

    boolean allEntries() default false;

    boolean beforeInvocation() default false;

}


package com.kaochong.cache.client;

import com.kaochong.cache.annotation.KCCacheEvict;
import com.kaochong.cache.annotation.KCCachePut;
import com.kaochong.cache.annotation.KCCacheable;
import com.kaochong.cache.client.impl.EhcacheCache;
import com.kaochong.cache.enums.CacheType;
import com.kaochong.cache.utils.SPELUtil;
import com.kaochong.cache.utils.SerializableUtil;
import org.ehcache.Cache;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

import java.io.IOException;
import java.time.Duration;

public interface InterfaceCache {

    public void set(KCCacheable kcCacheable, String[] parameterNames, Object[] args, Object data) throws IOException;

    public Object get(KCCacheable kcCacheable, String[] parameterNames, Object[] args, Class returnType) throws IOException, ClassNotFoundException;

    public void set(KCCachePut kcCachePut, String[] parameterNames, Object[] args, Object data) throws IOException;

    public void del(KCCacheEvict kcCacheEvict, String[] parameterNames, Object[] args, Object data);

}

package com.kaochong.cache.aspect;

import com.kaochong.cache.adapter.CacheAdapter;
import com.kaochong.cache.annotation.KCCacheEvict;
import com.kaochong.cache.annotation.KCCachePut;
import com.kaochong.cache.annotation.KCCacheable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(0)
public class KCCacheAspect {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    CacheAdapter cacheAdapter;

    @Pointcut("@annotation(com.kaochong.cache.annotation.KCCacheable)")
    public void KCCacheable() {
    }

    @Pointcut("@annotation(com.kaochong.cache.annotation.KCCacheEvict)")
    public void KCCacheEvict() {
    }

    @Pointcut("@annotation(com.kaochong.cache.annotation.KCCachePut)")
    public void KCCachePut() {
    }

    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("KCCacheable()")
    public Object KCCacheable(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        //获取注解
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        KCCacheable annotation = targetMethod.getAnnotation(KCCacheable.class);
        //获取返回类型
        Class returnType = methodSignature.getReturnType();
        //获取方法参数与对象
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = u.getParameterNames(((MethodSignature) proceedingJoinPoint.getSignature()).getMethod());
        Object[] args = proceedingJoinPoint.getArgs();
        //读取缓存
        Object proceed = cacheAdapter.get(annotation, parameterNames, args, returnType);
        if (proceed == null) {
            proceed = proceedingJoinPoint.proceed();
            //写入缓存
            cacheAdapter.set(annotation, parameterNames, args, proceed);
        }
        return proceed;
    }


    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("KCCacheEvict()")
    public Object KCCacheEvict(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object proceed = null;
        //获取注解
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        KCCacheEvict annotation = targetMethod.getAnnotation(KCCacheEvict.class);
        //获取方法参数与对象
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = u.getParameterNames(((MethodSignature) proceedingJoinPoint.getSignature()).getMethod());
        Object[] args = proceedingJoinPoint.getArgs();
        //删除缓存
        cacheAdapter.del(annotation, parameterNames, args, proceed);
        proceed = proceedingJoinPoint.proceed();
        return proceed;
    }


    //环绕通知,环绕增强，相当于MethodInterceptor
    @Around("KCCachePut()")
    public Object KCCachePut(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object proceed = null;
        proceed = proceedingJoinPoint.proceed();

        //获取注解
        Signature signature = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        KCCachePut annotation = targetMethod.getAnnotation(KCCachePut.class);
        //获取返回类型
        Class returnType = methodSignature.getReturnType();
        //获取方法参数与对象
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] parameterNames = u.getParameterNames(((MethodSignature) proceedingJoinPoint.getSignature()).getMethod());
        Object[] args = proceedingJoinPoint.getArgs();
        cacheAdapter.set(annotation, parameterNames, args, proceed);

        return proceed;
    }

}


# kc-boot-cache-starter
项目编写起因：spring cache 注解不提供缓存失效策略，项目中不同数据缓存有效期时间不同，因此为提供快死开发编写次工具辅助

主要实现redis 失效策略自定义 ehcache失效策略自定义.

代码示例：

    @KCCacheEvict(cacheType = CacheType.REDIS, cacheNames = "listen:THearUser", key = "#tHearUser.openId")
    public void updateByPrimaryKeySelective(THearUser tHearUser) {

    @KCCacheable(cacheType = CacheType.EHCACHE, cacheNames = "listen:HearUser", key = "#token", unless = "#result != null", ehcacheSize = 10000, ehcacheIdleExpire = 10)
    public HearUser selectByToken(String token) {

使用配置说明：

1.加入maven依赖
        <dependency>
            <groupId>com.kaochong.cache</groupId>
            <artifactId>kc-boot-cache-starter</artifactId>
            <version>0.0.3-SNAPSHOT</version>
        </dependency>
        
        
2.开启切面
        加入注解 @EnableAspectJAutoProxy
        
到这里就可以愉快到使用注解方式使用缓存了

注意：项目依赖
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

package com.evan.mall.common.cache;

import com.alibaba.fastjson.JSON;
import com.evan.mall.common.constant.RedisConst;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author atguigu-mqx
 */
@Component
@Aspect
@SuppressWarnings("all")
@Slf4j
public class GmallCacheAspect {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;


    //  定义一个环绕通知！

    /**
     * 定义一个环绕通知！
     *
     * @param proceedingJoinPoint
     * @return RedisCache标注的方法的返回值
     * 业务逻辑！
     * 1. 必须先知道这个注解在哪些方法 || 必须要获取到方法上的注解
     * 2. 获取到注解上的前缀
     * 3. 必须要组成一个缓存的key！
     * 4. 可以通过这个key 获取缓存的数据
     * true:
     * 直接返回！
     * false:
     * 分布式锁业务逻辑！
     */
    @SneakyThrows
    @Around("@annotation(com.evan.mall.common.cache.RedisCache)")
    public Object redisCacheAspectMethod(ProceedingJoinPoint proceedingJoinPoint) {
        //  定义一个对象
        Object obj = new Object();

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        RedisCache redisCache = methodSignature.getMethod().getAnnotation(RedisCache.class);
        //   获取到注解上的前缀
        String prefix = redisCache.prefix();
        //  组成缓存的key！ 获取方法传递的参数
        Object[] args = proceedingJoinPoint.getArgs();
        String key = prefix + Arrays.asList(args).toString() + redisCache.suffix();
        try {
            //  可以通过这个key 获取缓存的数据
            //obj = this.getRedisData(key, methodSignature);
            obj = this.getRedisDataObject(key);
            if (obj == null) {
                //  分布式业务逻辑
                //  设置分布式锁，进入数据库进行查询数据！
                String lockKey = key + ":lock";
                RLock lock = redissonClient.getLock(lockKey);
                //  调用trylock方法
                boolean result = lock.tryLock(RedisConst.SKULOCK_EXPIRE_PX1, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS);
                //  判断
                if (result) {
                    try {
                        //  执行业务逻辑：直接从数据库获取数据
                        obj = proceedingJoinPoint.proceed(args);
                        //  防止缓存穿透
                        if (obj == null) {
                            Class returnType = methodSignature.getReturnType();
                            obj = returnType.getDeclaredConstructor().newInstance();
                            //  将缓存的数据变为 Json 的 字符串
//                            this.redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
                            this.redisTemplate.opsForValue().set(key, obj, RedisConst.SKUKEY_TEMPORARY_TIMEOUT, TimeUnit.SECONDS);
                            return obj;
                        }
                        //  将缓存的数据变为 Json 的 字符串
//                        this.redisTemplate.opsForValue().set(key, JSON.toJSONString(obj), RedisConst.SKUKEY_TIMEOUT, TimeUnit.SECONDS);
                        this.redisTemplate.opsForValue().set(key, obj, RedisConst.SKUKEY_TIMEOUT, TimeUnit.SECONDS);
                        return obj;
                    } finally {
                        //  解锁
                        lock.unlock();
                    }
                } else {
                    //  没有获取到
                    Thread.sleep(100);
                    return redisCacheAspectMethod(proceedingJoinPoint);
                }
            } else {
                //  直接从缓存获取的数据！
                return obj;
            }
        } catch (Exception exception) {
            log.error("RedisCache增强出现异常了！" + exception.getMessage());
        }
        //  数据库兜底！
        return proceedingJoinPoint.proceed(args);
    }

    /**
     * 从缓存中获取数据！
     *
     * @param key
     * @return
     */
    private Object getRedisData(String key, MethodSignature methodSignature) {
        //  在向缓存存储数据的时候，将数据变为Json 字符串了！
        //  通过这个key 获取到缓存的value
        String strJson = (String) this.redisTemplate.opsForValue().get(key);
        //  判断
        if (!StringUtils.isEmpty(strJson)) {
            //  将字符串转换为对应的数据类型！
            return JSON.parseObject(strJson, methodSignature.getReturnType());
        }
        return null;
    }

    private Object getRedisDataObject(String key) {
        return this.redisTemplate.opsForValue().get(key);
    }

}

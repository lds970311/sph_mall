package com.evan.mall.common.cache;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author atguigu-mqx
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisCache {
    @AliasFor("prefix")
    String value() default "cache:";

    //  定义一个数据 sku:skuId
    //  目的用这个前缀要想组成 缓存的key！
    @AliasFor("value")
    String prefix() default "cache:";

    String suffix() default ":info";

}

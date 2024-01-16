package com.hikvision.commons.cache.aspectj.annotation;

import java.lang.annotation.*;

/**
 * 同时使用多个缓存注解
 *
 * @author olafwang
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Caching {
    Cacheable[] cacheable() default {};

    CachePut[] put() default {};

    CacheEvict[] evict() default {};
}

package com.hikvision.commons.core.annotation;

import com.google.common.base.Predicate;
import com.hikvision.commons.core.DefaultPredicate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 重试注解
 * @author zhaolin16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
    //出现Exception时重试
    boolean retryIfException() default false;

    //程序出现RuntimeException异常时重试
    boolean retryIfRuntimeException() default false;

    //重试次数
    int attemptNumber() default 3;

    //重试间隔 ms
    long waitStrategySleepTime() default 1000;

    //持续时间; 期间
    long duration() default 0;

    /**
     * 返回结果的条件
     */
    Class<? extends Predicate> returnResultPredicate() default DefaultPredicate.class;
}

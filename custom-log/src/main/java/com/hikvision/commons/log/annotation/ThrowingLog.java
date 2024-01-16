package com.hikvision.commons.log.annotation;

import com.hikvision.commons.log.callback.LogCallback;
import com.hikvision.commons.log.enums.ReadWriteType;

import java.lang.annotation.*;

/**
 * 异常日志
 * @author zhaolin16
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ThrowingLog {
    /**
     * 业务名称
     */
    String value() default "";

    /**
     * 读写类型
     */
    ReadWriteType readWriteType() default ReadWriteType.READ;

    /**
     * 回调
     */
    Class<? extends LogCallback> callback() default LogCallback.class;
}


package com.hikvision.commons.log.annotation;


import com.hikvision.commons.log.callback.LogCallback;
import com.hikvision.commons.log.enums.Level;
import com.hikvision.commons.log.enums.Position;
import com.hikvision.commons.log.enums.ReadWriteType;
import com.hikvision.commons.log.formatter.DefaultLogFormatter;
import com.hikvision.commons.log.formatter.LogFormatter;

import java.lang.annotation.*;

/**
 * 综合日志
 * @author zhaolin16
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    /**
     * 业务名称
     */
    String value() default "";

    /**
     * 日志级别
     */
    Level level() default Level.UNKNOWN;

    /**
     * 代码定位支持
     */
    Position position() default Position.UNKNOWN;

    /**
     * 读写类型
     */
    ReadWriteType readWriteType() default ReadWriteType.READ;

    /**
     * 参数过滤
     */
    String[] paramFilter() default {};

    /**
     * 格式化
     */
    Class<? extends LogFormatter> formatter() default DefaultLogFormatter.class;

    /**
     * 回调
     */
    Class<? extends LogCallback> callback() default LogCallback.class;
}

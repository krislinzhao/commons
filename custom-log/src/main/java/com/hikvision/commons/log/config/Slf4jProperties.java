package com.hikvision.commons.log.config;

import com.hikvision.commons.log.annotation.Log;
import com.hikvision.commons.log.callback.LogCallback;
import com.hikvision.commons.log.callback.VoidLogCallback;
import com.hikvision.commons.log.enums.Level;
import com.hikvision.commons.log.enums.Position;
import com.hikvision.commons.log.formatter.*;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日志配置
 * @author zhaolin16
 */
@Data
@Configuration("slf4jProperties")
@ConfigurationProperties(prefix = "logging.slf4j")
public class Slf4jProperties implements InitializingBean {

    /**
     *  全局综合日志级别
     */
    private Level globalLogLevel = Level.DEBUG;

    /**
     *  全局综合日志代码定位
     */
    private Position globalLogPosition = Position.UNKNOWN;

    /**
     * 全局综合日志格式化
     */
    private Class<? extends LogFormatter> globalLogFormatter = DefaultLogFormatter.class;

    /**
     * 全局综合日志回调
     */
    private Class<? extends LogCallback> globalLogCallback = VoidLogCallback.class;

    /**
     * 全局参数日志级别
     */
    private Level globalParamLogLevel = Level.DEBUG;

    /**
     *  全局参数日志代码定位
     */
    private Position globalParamLogPosition = Position.UNKNOWN;

    /**
     * 全局参数日志格式化
     */
    private Class<? extends ParamLogFormatter> globalParamLogFormatter = DefaultParamLogFormatter.class;

    /**
     * 全局参数日志回调
     */
    private Class<? extends LogCallback> globalParamLogCallback = VoidLogCallback.class;

    /**
     * 全局结果日志级别
     */
    private Level globalResultLogLevel = Level.DEBUG;

    /**
     *  全局结果日志代码定位
     */
    private Position globalResultLogPosition = Position.UNKNOWN;

    /**
     * 全局结果日志格式化
     */
    private Class<? extends ResultLogFormatter> globalResultLogFormatter = DefaultResultLogFormatter.class;

    /**
     * 全局结果日志回调
     */
    private Class<? extends LogCallback> globalResultLogCallback = VoidLogCallback.class;

    /**
     * 全局异常日志回调
     */
    private Class<? extends LogCallback> globalThrowingLogCallback = VoidLogCallback.class;

    /**
     * 回调容器
     */
    private final Map<String, Class<? extends LogCallback>> callbackContainer = new ConcurrentHashMap<>(4);

    @Override
    public void afterPropertiesSet() throws Exception {
        this.callbackContainer.put(Log.class.getName(), this.globalLogCallback);
//        this.callbackContainer.put(ParamLog.class.getName(), this.globalParamLogCallback);
//        this.callbackContainer.put(ResultLog.class.getName(), this.globalResultLogCallback);
//        this.callbackContainer.put(ThrowingLog.class.getName(), this.globalThrowingLogCallback);
    }

    public Class<? extends LogCallback> getCallback(Annotation annotation) {
        return this.callbackContainer.get(annotation.annotationType().getName());
    }
}

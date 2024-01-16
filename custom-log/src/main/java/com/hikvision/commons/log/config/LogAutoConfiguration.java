package com.hikvision.commons.log.config;

import com.hikvision.commons.core.support.AppContext;
import com.hikvision.commons.log.LogProcessor;
import com.hikvision.commons.log.callback.VoidLogCallback;
import com.hikvision.commons.log.formatter.DefaultLogFormatter;
import com.hikvision.commons.log.formatter.DefaultParamLogFormatter;
import com.hikvision.commons.log.formatter.DefaultResultLogFormatter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 日志自动装配
 * @author zhaolin16
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(Slf4jProperties.class)
@ConditionalOnClass({Logger.class})
@Import({
        AppContext.class,
        DefaultLogFormatter.class,
        DefaultParamLogFormatter.class,
        DefaultResultLogFormatter.class,
        VoidLogCallback.class
})
public class LogAutoConfiguration {
    @Bean
    public LogProcessor logProcessor() {
        log.info("LogAutoConfiguration load");
        return new LogProcessor();
    }
}

package com.hikvision.commons.exception.config;


import com.hikvision.commons.exception.handler.UnifiedExceptionHandler;
import com.hikvision.commons.exception.i18n.UnifiedMessageSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 自动装配
 */
@Slf4j
@Configuration
@Import({
        UnifiedExceptionHandler.class,
        UnifiedMessageSource.class
})
public class ExceptionAutoConfiguration {
    @Bean
    public UnifiedExceptionHandler unifiedExceptionHandler() {
        log.info("ExceptionAutoConfiguration load");
        return new UnifiedExceptionHandler();
    }
}

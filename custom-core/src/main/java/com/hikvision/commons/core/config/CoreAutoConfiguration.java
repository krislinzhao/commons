package com.hikvision.commons.core.config;

import com.hikvision.commons.core.aop.RetryAspect;
import com.hikvision.commons.core.support.AppContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @description: core自动装配
 * @author zhaolin16
 */
@Slf4j
@Configuration
@Import({
        AppContext.class
})
public class CoreAutoConfiguration {
    @Bean
    public RetryAspect retryProcessor() {
        log.info("CoreAutoConfiguration retryProcessor load");
        return new RetryAspect();
    }
}

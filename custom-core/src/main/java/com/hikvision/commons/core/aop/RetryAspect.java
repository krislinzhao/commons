package com.hikvision.commons.core.aop;

import cn.hutool.extra.spring.SpringUtil;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import com.google.common.base.Predicate;
import com.hikvision.commons.core.DefaultPredicate;
import com.hikvision.commons.core.annotation.Retry;
import com.hikvision.commons.core.support.AppContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;


/**
 * @description: 重试注解切面
 * @author zhaolin16
 */
@Slf4j
@Aspect
public class RetryAspect {
    @Around(value = "@annotation(com.hikvision.commons.core.annotation.Retry)")
    public Object monitorAround(ProceedingJoinPoint pjp) throws Throwable {
        log.info("enter retry aspect");
        Method method;
        //判断注解是否method 上
        if (pjp.getSignature() instanceof MethodSignature) {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            method = signature.getMethod();
        } else {
            return null;
        }
        Retry annotation = method.getDeclaredAnnotation(Retry.class);
        //重试时间，重试次数
        if (annotation.duration() <= 0 && annotation.attemptNumber() <= 1) {
            return pjp.proceed();
        }

        RetryerBuilder builder = RetryerBuilder.newBuilder();

        //重试次数
        if (annotation.attemptNumber() > 0) {
            builder.withStopStrategy(StopStrategies.stopAfterAttempt(annotation.attemptNumber()));
        }
        //退出策略
        if (annotation.duration() > 0) {
            builder.withStopStrategy(StopStrategies.stopAfterDelay(annotation.duration(), TimeUnit.MILLISECONDS));
        }

        //重试间间隔等待策略
        if (annotation.waitStrategySleepTime() > 0) {
            builder.withWaitStrategy(WaitStrategies.fixedWait(annotation.waitStrategySleepTime(), TimeUnit.MILLISECONDS));
        }

        //RuntimeException时重试
        if (annotation.retryIfRuntimeException()){
            builder.retryIfRuntimeException();

        }
        //Exception时重试
        if (annotation.retryIfException()){
            builder.retryIfException();
        }

        //指定返回重试
        if (!annotation.returnResultPredicate().getName().equals(DefaultPredicate.class.getName())) {
            Predicate predicate = AppContext.getContext().getBean(annotation.returnResultPredicate());
            builder.retryIfResult(predicate);
        }

        return builder.build().call(() -> {
            try {
                log.info("aspect exec method "+   method.getName());
                return pjp.proceed();
            } catch (Throwable throwable) {
                if (throwable instanceof Exception) {
                    throw (Exception) throwable;
                } else {
                    throw new Exception(throwable);
                }
            }
        });
    }
}

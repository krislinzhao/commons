package com.hikvision.commons.log;

import com.hikvision.commons.core.support.AppContext;
import com.hikvision.commons.log.annotation.Log;
import com.hikvision.commons.log.annotation.ThrowingLog;
import com.hikvision.commons.log.callback.LogCallback;
import com.hikvision.commons.log.config.Slf4jProperties;
import com.hikvision.commons.log.enums.Level;
import com.hikvision.commons.log.enums.Position;
import com.hikvision.commons.log.formatter.DefaultLogFormatter;
import com.hikvision.commons.log.formatter.LogFormatter;
import com.hikvision.commons.log.support.LogHandler;
import com.hikvision.commons.log.support.MethodInfo;
import com.hikvision.commons.log.support.MethodParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 日志处理器
 * @author zhaolin16
 */
@Slf4j
@Aspect
public class LogProcessor {
    @Resource(type = Slf4jProperties.class)
    private Slf4jProperties slf4jProperties;

    /**
     * 打印环绕日志
     * @param joinPoint 切入点
     * @return 返回方法返回值
     * @throws Throwable 异常
     */
    @Around(value = "@annotation(com.hikvision.commons.log.annotation.Log)")
    public Object aroundPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature  = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Object result;
        if (LogHandler.isEnable(log)) {
            long beginTime = System.currentTimeMillis();
            long endTime;
            try {
                result = joinPoint.proceed(args);
                endTime = System.currentTimeMillis();
            }catch (Exception e) {
                result = e;
                endTime = System.currentTimeMillis();
            }
            MethodInfo methodInfo;
            try {
                Log annotation = signature.getMethod().getAnnotation(Log.class);
                Level level = annotation.level()==Level.UNKNOWN?this.slf4jProperties.getGlobalLogLevel():annotation.level();
                Position position = annotation.position()==Position.UNKNOWN?this.slf4jProperties.getGlobalLogPosition():annotation.position();
                if (position==Position.ENABLED||(position==Position.UNKNOWN&&level==Level.DEBUG)) {
                    methodInfo = MethodParser.getMethodInfo(signature);
                }else {
                    methodInfo = MethodParser.getMethodInfo(MethodInfo.NATIVE_LINE_NUMBER, signature);
                }
                methodInfo.setTakeTime(endTime-beginTime);
                LogFormatter formatter;
                Class<? extends LogFormatter> formatType = annotation.formatter();
                if (!formatType.getName().equals(DefaultLogFormatter.DEFAULT)) {
                    formatter = AppContext.getContext().getBean(formatType);
                }else {
                    formatter = AppContext.getContext().getBean(this.slf4jProperties.getGlobalLogFormatter());
                }
                formatter.format(log, level, annotation.value(), methodInfo, args, annotation.paramFilter(), result);
                // 执行回调
                this.callback(annotation.callback(), annotation, methodInfo, joinPoint, result);
            }catch (Exception e) {
                log.error("{}.{}方法错误", signature.getDeclaringTypeName(), signature.getMethod().getName());
                log.error(e.getMessage(), e);
            }
            if (result instanceof Throwable) {
                throw (Throwable) result;
            }
        }else {
            result = joinPoint.proceed(args);
        }
        return result;
    }

    /**
     * 打印异常日志
     * @param joinPoint 切入点
     * @param throwable 异常
     */
    @AfterThrowing(
            value = "@annotation(com.hikvision.commons.log.annotation.ThrowingLog)||@annotation(com.hikvision.commons.log.annotation.Log)",
            throwing = "throwable"
    )
    public void throwingPrint(JoinPoint joinPoint, Throwable throwable) {
        if (LogHandler.isEnable(log)) {
            MethodSignature signature  = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            String methodName = method.getName();
            try {
                Annotation annotation;
                String busName;
                Class<? extends LogCallback> callback;
                MethodInfo methodInfo = MethodParser.getMethodInfo(MethodInfo.NATIVE_LINE_NUMBER, signature);
                ThrowingLog throwingLogAnnotation = method.getAnnotation(ThrowingLog.class);
                if (throwingLogAnnotation!=null) {
                    annotation = throwingLogAnnotation;
                    busName = throwingLogAnnotation.value();
                    callback = throwingLogAnnotation.callback();
                }else {
                    Log logAnnotation = method.getAnnotation(Log.class);
                    annotation = logAnnotation;
                    busName = logAnnotation.value();
                    callback = logAnnotation.callback();
                }
                log.error(LogHandler.getThrowingInfo(busName, methodInfo)+throwable.getLocalizedMessage(), throwable);
                // 执行回调
                this.callback(callback, annotation, methodInfo, joinPoint, null);
            } catch (Exception e) {
                log.error("{}.{}方法错误", signature.getDeclaringTypeName(), methodName);
                log.error(e.getMessage(), e);
            }
        }
    }


    /**
     * 执行回调
     * @param callback 回调类
     * @param annotation 触发注解
     * @param methodInfo 方法信息
     * @param joinPoint 切入点
     * @param result 方法结果
     */
    private void callback(
            Class<? extends LogCallback> callback,
            Annotation annotation,
            MethodInfo methodInfo,
            JoinPoint joinPoint,
            Object result
    ) {
        try {
            if (!callback.getName().equals(LogCallback.class.getName())) {
                AppContext.getContext().getBean(callback).callback(
                        annotation,
                        methodInfo,
                        LogHandler.getNotParseParamMap(methodInfo.getParamNames(), joinPoint.getArgs(), null),
                        result
                );
            }else {
                AppContext.getContext().getBean(this.slf4jProperties.getCallback(annotation)).callback(
                        annotation,
                        methodInfo,
                        LogHandler.getNotParseParamMap(methodInfo.getParamNames(), joinPoint.getArgs(), null),
                        result
                );
            }
        }catch (Exception ex) {
            log.error("{}.{}方法日志回调错误：[{}]", methodInfo.getClassAllName(), methodInfo.getMethodName(), ex.getMessage());
        }
    }
}

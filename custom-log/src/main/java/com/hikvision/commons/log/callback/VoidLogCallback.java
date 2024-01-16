package com.hikvision.commons.log.callback;

import com.hikvision.commons.log.support.MethodInfo;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 默认空的回调
 * @author zhaolin16
 */
public final class VoidLogCallback implements LogCallback {
    /**
     * 回调方法
     *
     * @param annotation 当前使用注解
     * @param methodInfo 方法信息
     * @param paramMap   参数字典
     * @param result     方法调用结果
     */
    @Override
    public void callback(Annotation annotation, MethodInfo methodInfo, Map<String, Object> paramMap, Object result) {
    }
}

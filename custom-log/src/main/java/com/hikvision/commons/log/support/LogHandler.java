package com.hikvision.commons.log.support;

import com.hikvision.commons.log.enums.Level;
import org.slf4j.Logger;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 日志工具
 * @author zhaolin16
 */
public class LogHandler {
    /**
     * 获取日志信息字符串
     * @param busName 业务名
     * @param methodInfo 方法信息
     * @param params 参数值
     * @param filterParamNames 过滤参数列表
     * @return 返回日志信息字符串
     */
    public static String getBeforeInfo(
            String busName,
            MethodInfo methodInfo,
            Object[] params,
            String[] filterParamNames
    ) {
        return createInfoBuilder(busName, methodInfo).append("接收参数:[").append(
                getParamMap(
                        methodInfo.getParamNames(),
                        params,
                        filterParamNames
                )
        ).append("]").toString();
    }

    /**
     * 获取日志信息字符串
     * @param busName 业务名
     * @param methodInfo 方法信息
     * @param result 返回结果
     * @return 返回日志信息字符串
     */
    public static String getAfterInfo(String busName, MethodInfo methodInfo, Object result) {
        return createInfoBuilder(busName, methodInfo).append("返回结果：[").append(parseParam(result)).append(']').toString();
    }

    /**
     * 获取日志信息字符串
     * @param busName 业务名
     * @param methodInfo 方法信息
     * @param params 参数值
     * @param filterParamNames 过滤参数列表
     * @param result 返回结果
     * @return 返回日志信息字符串
     */
    public static String getAroundInfo(
            String busName,
            MethodInfo methodInfo,
            Object[] params,
            String[] filterParamNames,
            Object result
    ) {
        return createInfoBuilder(busName, methodInfo).append("接收参数:[").append(
                        getParamMap(
                                methodInfo.getParamNames(),
                                params,
                                filterParamNames
                        )
                ).append("],").append("返回结果:[")
                .append(
                        result instanceof Throwable?
                                "执行异常("+((Throwable) result).getLocalizedMessage()+")":
                                parseParam(result)
                ).append("],耗时:").append(methodInfo.getTakeTime()).append("ms").toString();
    }

    /**
     * 获取日志信息字符串
     * @param busName 业务名
     * @param methodInfo 方法信息
     * @return 返回日志信息字符串
     */
    public static String getThrowingInfo(String busName, MethodInfo methodInfo) {
        return createInfoBuilder(busName, methodInfo).append("异常信息:").toString();
    }

    /**
     * 是否参数过滤
     * @param filterParamNames 过滤参数名称列表
     * @param paramName 带过滤参数名称
     * @return 返回布尔值，过滤true，否则false
     */
    public static boolean isParamFilter(String[] filterParamNames, String paramName) {
        for (String name : filterParamNames) {
            if (name.equals(paramName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 创建日志信息builder
     * @param busName 业务名
     * @param methodInfo 方法信息
     * @return 返回日志信息builder
     */
    public static StringBuilder createInfoBuilder(String busName, MethodInfo methodInfo) {
        StringBuilder builder = new StringBuilder();
        builder.append("调用方法:[");
        if (methodInfo.isNative()) {
            builder.append(methodInfo.getClassAllName()).append('.').append(methodInfo.getMethodName());
        }else {
            builder.append(createMethodStack(methodInfo));
        }
        return builder.append("],").append("业务名称:[").append(busName).append("],");
    }

    /**
     * 获取参数字典
     * @param paramNames 参数名称列表
     * @param paramValues 参数值数组
     * @return 返回参数字典
     */
    public static Map<String, Object> getParamMap(List<String> paramNames, Object[] paramValues) {
        return getParamMap(paramNames, paramValues, null);
    }

    /**
     * 获取参数字典
     * @param paramNames 参数名称列表
     * @param paramValues 参数值数组
     * @param filterParamNames 过滤参数列表
     * @return 返回参数字典
     */
    public static Map<String, Object> getParamMap(List<String> paramNames, Object[] paramValues, String[] filterParamNames) {
        Map<String, Object> paramMap = getNotParseParamMap(paramNames, paramValues, filterParamNames);
        if (!paramMap.isEmpty()) {
            Map<String, Object> newMap = new LinkedHashMap<>(paramMap.size());
            Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
            for (Map.Entry<String, Object> entry : entries) {
                newMap.put(entry.getKey(), parseParam(entry.getValue()));
            }
            paramMap = newMap;
        }
        return paramMap;
    }

    /**
     * 获取未解析参数字典
     * @param paramNames 参数名称列表
     * @param paramValues 参数值数组
     * @param filterParamNames 过滤参数列表
     * @return 返回参数字典
     */
    public static Map<String, Object> getNotParseParamMap(List<String> paramNames, Object[] paramValues, String[] filterParamNames) {
        int count = paramNames.size();
        Map<String, Object> paramMap = new LinkedHashMap<>(count);
        if (count>0) {
            if (filterParamNames!=null&&filterParamNames.length>0) {
                for (int i = 0; i < count; i++) {
                    if (!isParamFilter(filterParamNames, paramNames.get(i))) {
                        paramMap.put(paramNames.get(i), paramValues[i]);
                    }
                }
            }else {
                for (int i = 0; i < count; i++) {
                    paramMap.put(paramNames.get(i), paramValues[i]);
                }
            }
        }
        return paramMap;
    }

    /**
     * 解析参数
     * @param param 参数
     * @return 返回参数字符串
     */
    public static String parseParam(Object param) {
        if (param==null) {
            return null;
        }
        Class<?> paramClass = param.getClass();
        if (Map.class.isAssignableFrom(paramClass)) {
            return parseMap(param);
        }
        if (Iterable.class.isAssignableFrom(paramClass)) {
            return parseIterable(param);
        }
        return paramClass.isArray() ? parseArray(param): param.toString();
    }

    /**
     * 解析字典
     * @param param 参数值
     * @return 返回参数字典字符串
     */
    public static String parseMap(Object param) {
        Map<?, ?> paramMap = (Map<?, ?>) param;
        Iterator<? extends Map.Entry<?, ?>> iterator = paramMap.entrySet().iterator();
        if (!iterator.hasNext()) {
            return "{}";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        Map.Entry<?, ?> entry;
        while (true) {
            entry = iterator.next();
            builder.append(entry.getKey()).append('=').append(parseParam(entry.getValue()));
            if (iterator.hasNext()) {
                builder.append(',').append(' ');
            }else {
                return builder.append('}').toString();
            }
        }
    }

    /**
     * 解析集合
     * @param param 参数值
     * @return 返回参数列表字符串
     */
    public static String parseIterable(Object param) {
        Iterator<?> iterator = ((Iterable<?>) param).iterator();
        if (!iterator.hasNext()) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        while (true) {
            builder.append(parseParam(iterator.next()));
            if (iterator.hasNext()) {
                builder.append(',').append(' ');
            }else {
                return builder.append(']').toString();
            }
        }
    }

    /**
     * 解析数组
     * @param param 参数值
     * @return 返回参数列表字符串
     */
    public static String parseArray(Object param) {
        int length = Array.getLength(param);
        if (length==0) {
            return "[]";
        }
        StringBuilder builder = new StringBuilder();
        builder.append('[');
        for (int i = 0; i < length; i++) {
            builder.append(parseParam(Array.get(param, i)));
            if (i+1<length){
                builder.append(',').append(' ');
            }
        }
        return builder.append(']').toString();
    }

    /**
     * 创建方法栈
     * @param methodInfo 方法信息
     * @return 返回栈信息
     */
    public static StackTraceElement createMethodStack(MethodInfo methodInfo) {
        return new StackTraceElement(
                methodInfo.getClassAllName(),
                methodInfo.getMethodName(),
                methodInfo.getClassSimpleName()+".java",
                methodInfo.getLineNumber()
        );
    }

    /**
     * 打印信息
     * @param log 日志对象
     * @param level 日志级别
     * @param msg 输出信息
     */
    public static void print(Logger log, Level level, String msg) {
        switch (level) {
            case DEBUG: log.debug(msg); break;
            case INFO: log.info(msg); break;
            case WARN: log.warn(msg); break;
            case ERROR: log.error(msg); break;
            default:
        }
    }

    /**
     * 判断是否开启打印
     * @param log 日志对象
     * @return 返回布尔值
     */
    public static boolean isEnable(Logger log) {
        return log.isDebugEnabled()||
                log.isInfoEnabled()||
                log.isWarnEnabled()||
                log.isErrorEnabled()||
                log.isTraceEnabled();
    }
}

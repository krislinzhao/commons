package com.hikvision.commons.log.formatter;

import com.hikvision.commons.log.enums.Level;
import com.hikvision.commons.log.support.LogHandler;
import com.hikvision.commons.log.support.MethodInfo;
import org.slf4j.Logger;

/**
 * 默认综合日志格式化实现
 * @author zhaolin16
 */
public class DefaultLogFormatter implements LogFormatter {
    /**
     * 默认实现名称
     */
    public static final String DEFAULT = DefaultLogFormatter.class.getName();

    /**
     * 格式化
     *
     * @param log              日志对象
     * @param level            日志级别
     * @param busName          业务名称
     * @param methodInfo       方法信息
     * @param args             参数列表
     * @param filterParamNames 参数过滤列表
     * @param result           返回结果
     */
    @Override
    public void format(Logger log, Level level, String busName, MethodInfo methodInfo, Object[] args, String[] filterParamNames, Object result) {
        LogHandler.print(log, level, LogHandler.getAroundInfo(busName, methodInfo, args, filterParamNames, result));
    }
}

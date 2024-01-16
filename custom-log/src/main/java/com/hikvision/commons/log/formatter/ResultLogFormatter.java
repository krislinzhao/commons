package com.hikvision.commons.log.formatter;

import com.hikvision.commons.log.enums.Level;
import com.hikvision.commons.log.support.MethodInfo;
import org.slf4j.Logger;

/**
 * 结果日志格式化
 * @author zhaolin16
 */
public interface ResultLogFormatter {
    /**
     * 格式化
     * @param log 日志对象
     * @param level 日志级别
     * @param busName 业务名称
     * @param methodInfo 方法信息
     * @param result 返回结果
     */
    void format(
            Logger log,
            Level level,
            String busName,
            MethodInfo methodInfo,
            Object result
    );
}

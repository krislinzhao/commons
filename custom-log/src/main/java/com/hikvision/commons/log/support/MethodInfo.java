package com.hikvision.commons.log.support;

import com.hikvision.commons.log.annotation.Log;
import lombok.Data;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

/**
 * 方法信息
 * @author zhaolin16
 */
@Data
public class MethodInfo {

    /**
     * 代表本地方法，不进行代码定位
     */
    public static final int NATIVE_LINE_NUMBER = -2;

    /**
     * 所在类全类名
     */
    private String classAllName;
    /**
     * 所在类简单类名
     */
    private String classSimpleName;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数列表
     */
    private List<String> paramNames;
    /**
     * 方法行号
     */
    private Integer lineNumber;
    /**
     * 方法签名
     */
    private MethodSignature signature;
    /**
     * 方法耗时(单位：ms)
     * <p>注：仅{@link Log @Log}注解有值</p>
     */
    private Long takeTime;

    /**
     * 构造
     * @param classAllName 所在类全类名
     * @param classSimpleName 所在类简单类名
     * @param methodName 方法名称
     * @param paramNames 参数列表
     * @param lineNumber 方法行号
     * @param signature 方法签名
     */
    MethodInfo(String classAllName, String classSimpleName, String methodName, List<String> paramNames, Integer lineNumber, MethodSignature signature) {
        this.classAllName = classAllName;
        this.classSimpleName = classSimpleName;
        this.methodName = methodName;
        this.paramNames = paramNames;
        this.lineNumber = lineNumber;
        this.signature = signature;
    }

    /**
     * 是否本地方法
     * @return 返回布尔值
     */
    public boolean isNative() {
        return this.lineNumber == NATIVE_LINE_NUMBER;
    }
}

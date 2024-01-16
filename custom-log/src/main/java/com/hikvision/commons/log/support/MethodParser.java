package com.hikvision.commons.log.support;

import javassist.*;
import javassist.bytecode.LocalVariableAttribute;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * 方法解析器
 * @author zhaolin16
 */
public class MethodParser {
    /**
     * 类池
     */
    private static final ClassPool POOL = initClassPool();

    /**
     * 初始化类池
     * @return 返回类池
     */
    private static ClassPool initClassPool() {
        ClassPool pool = new ClassPool(true);
        pool.insertClassPath(new ClassClassPath(MethodParser.class));
        return pool;
    }

    /**
     * 获取调用方法
     * @param className 全类名
     * @param methodName 方法名称
     * @return 返回对应方法
     * @throws NotFoundException 未知方法异常
     */
    public static CtMethod getMethod(String className, String methodName) throws NotFoundException {
        return POOL.get(className).getDeclaredMethod(methodName);
    }

    /**
     * 获取方法信息
     * @param signature 方法签名
     * @return 返回方法信息
     */
    public static MethodInfo getMethodInfo(MethodSignature signature) {
        String className = signature.getDeclaringTypeName();
        String methodName = signature.getMethod().getName();
        try {
            return getMethodInfo(getMethod(signature.getDeclaringTypeName(), methodName), signature.getParameterNames(), signature);
        } catch (Exception e) {
            return new MethodInfo(
                    className,
                    className.substring(className.lastIndexOf('.')),
                    methodName,
                    new ArrayList<>(0),
                    -2,
                    signature
            );
        }
    }

    /**
     * 获取方法信息
     * @param method 方法对象
     * @param parameterNames 参数列表
     * @param signature 方法签名
     * @return 返回方法信息
     */
    public static MethodInfo getMethodInfo(CtMethod method, String[] parameterNames, MethodSignature signature) {
        CtClass declaringClass = method.getDeclaringClass();
        try {
            javassist.bytecode.MethodInfo methodInfo = method.getMethodInfo();
            int lineNumber = methodInfo.getLineNumber(0);
            List<String> paramNames;
            if (parameterNames!=null) {
                paramNames = new ArrayList<>(parameterNames.length);
                Collections.addAll(paramNames, parameterNames);
            }else {
                LocalVariableAttribute attribute = (LocalVariableAttribute) methodInfo.getCodeAttribute().getAttribute(LocalVariableAttribute.tag);
                if (attribute!=null) {
                    int count = method.getParameterTypes().length;
                    paramNames = new ArrayList<>(count);
                    for (int i = 1; i <= count; i++) {
                        paramNames.add(attribute.variableName(i));
                    }
                }else {
                    paramNames = new ArrayList<>(0);
                }
            }
            return new MethodInfo(declaringClass.getName(), declaringClass.getSimpleName(), method.getName(), paramNames, lineNumber, signature);
        } catch (Exception e) {
            return new MethodInfo(declaringClass.getName(), declaringClass.getSimpleName(), method.getName(), new ArrayList<>(0), -2, signature);
        }
    }

    /**
     * 获取方法信息
     * @param lineNumber 方法行号
     * @param signature 方法签名
     * @return 返回方法信息
     */
    public static MethodInfo getMethodInfo(int lineNumber, MethodSignature signature) {
        Class declaringClass = signature.getDeclaringType();
        String[] parameterNames = signature.getParameterNames();
        List<String> paramNames = new ArrayList<>(parameterNames.length);
        Collections.addAll(paramNames, parameterNames);
        return new MethodInfo(declaringClass.getName(), declaringClass.getSimpleName(), signature.getName(), paramNames, lineNumber, signature);
    }
}

package com.hikvision.commons.exception.constant.enums;

import com.hikvision.commons.exception.ArgumentException;
import com.hikvision.commons.exception.BaseException;
import com.hikvision.commons.exception.assertion.CommonExceptionAssert;
import com.hikvision.commons.exception.pojo.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用返回结果
 */
@Getter
@AllArgsConstructor
public enum CommonResponseEnum implements CommonExceptionAssert {
    /**
     * 成功
     */
    SUCCESS("0", "SUCCESS"),
    /**
     * 服务器繁忙，请稍后重试
     */
    SERVER_BUSY("9998", "服务器繁忙"),
    /**
     * 服务器异常，无法识别的异常，尽可能对通过判断减少未定义异常抛出
     */
    SERVER_ERROR("9999", "网络异常"),

    /**
     * 5***，一般对应于{@link ArgumentException}，系统封装的工具出现异常
     */

    // Time
    DATE_NOT_NULL("5001", "日期不能为空"),
    DATETIME_NOT_NULL("5001", "时间不能为空"),
    TIME_NOT_NULL("5001", "时间不能为空"),
    DATE_PATTERN_MISMATCH("5002", "日期[%s]与格式[%s]不匹配，无法解析"),
    PATTERN_NOT_NULL("5003", "日期格式不能为空"),
    PATTERN_INVALID("5003", "日期格式[%s]无法识别"),

    //other
    BUSINESS_ERROR("1001", "{0}"),
    FREQUENCY_LIMIT("-3", "请求太频繁了，请稍后再试哦~~"),
    ;

    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;

    /**
     * 校验返回结果是否成功
     * @param response 远程调用的响应
     */
    public static void assertSuccess(BaseResponse response) {
        SERVER_ERROR.assertNotNull(response);
        String code = response.getCode();
        if (CommonResponseEnum.SUCCESS.getCode() != code) {
            String msg = response.getMessage();
            throw new BaseException(code, msg);
        }
    }
}

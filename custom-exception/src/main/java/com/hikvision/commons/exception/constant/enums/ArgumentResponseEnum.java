package com.hikvision.commons.exception.constant.enums;

import com.hikvision.commons.exception.assertion.CommonExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 参数校验异常返回结果
 */
@Getter
@AllArgsConstructor
public enum ArgumentResponseEnum implements CommonExceptionAssert {
    /**
     * 绑定参数校验异常
     */
    VALID_ERROR("6000", "参数校验异常"),
    PARAM_VALID("-2", "参数校验失败{0}"),

    ;

    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;

}

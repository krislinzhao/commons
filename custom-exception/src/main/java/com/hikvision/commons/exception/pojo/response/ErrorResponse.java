package com.hikvision.commons.exception.pojo.response;

/**
 * <p>错误返回结果</p>
 */
public class ErrorResponse extends BaseResponse {

    public ErrorResponse() {
    }

    public ErrorResponse(String code, String message) {
        super(code, message);
    }
}

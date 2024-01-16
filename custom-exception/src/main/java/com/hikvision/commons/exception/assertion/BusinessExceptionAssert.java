package com.hikvision.commons.exception.assertion;

import cn.hutool.core.util.ArrayUtil;
import com.hikvision.commons.exception.BaseException;
import com.hikvision.commons.exception.BusinessException;
import com.hikvision.commons.exception.constant.IResponseEnum;

import java.text.MessageFormat;

/**
 * 业务异常断言
 */
public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }

        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }

        return new BusinessException(this, args, msg, t);
    }

}

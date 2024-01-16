package com.hikvision.commons.exception.assertion;

import cn.hutool.core.util.ArrayUtil;
import com.hikvision.commons.exception.ArgumentException;
import com.hikvision.commons.exception.BaseException;
import com.hikvision.commons.exception.constant.IResponseEnum;

import java.text.MessageFormat;

/**
 * 通用异常断言
 */
public interface CommonExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException(Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }

        return new ArgumentException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = this.getMessage();
        if (ArrayUtil.isNotEmpty(args)) {
            msg = MessageFormat.format(this.getMessage(), args);
        }

        return new ArgumentException(this, args, msg, t);
    }

}

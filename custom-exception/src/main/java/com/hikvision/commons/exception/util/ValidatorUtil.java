package com.hikvision.commons.exception.util;

import com.hikvision.commons.exception.ArgumentException;
import com.hikvision.commons.exception.constant.enums.ArgumentResponseEnum;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 参数校验工具类
 * @author zhaolin16
 */
public class ValidatorUtil {
    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 校验对象
     * @param object        待校验对象
     * @param groups        待校验的组
     * @throws ArgumentException  校验不通过，则报ArgumentException异常
     */
    public static void validateEntity(Object object, Class<?>... groups) throws ArgumentException {
        Set<ConstraintViolation<Object>> constraintViolations = VALIDATOR.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            for(ConstraintViolation<Object> constraint:  constraintViolations){
                msg.append(constraint.getMessage()).append("<br>");
            }
            throw new ArgumentException(ArgumentResponseEnum.VALID_ERROR, new Object[]{object}, msg.toString());
        }
    }
}

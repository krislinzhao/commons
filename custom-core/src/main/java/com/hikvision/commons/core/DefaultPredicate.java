package com.hikvision.commons.core;

import com.google.common.base.Predicate;

/**
 * @description: 默认的返回Predicate
 * @author zhaolin16
 */
public final class DefaultPredicate implements Predicate<Object> {
    @Override
    public boolean apply(Object input) {
        return false;
    }
}

package com.hikvision.commons.cache.support;

import cn.hutool.core.lang.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

public class SimpleKey implements Serializable {

    public static final SimpleKey EMPTY = new SimpleKey();

    private final Object[] params;
    private final int hashCode;


    /**
     * Create a new {@link SimpleKey} instance.
     *
     * @param elements the elements of the key
     */
    public SimpleKey(Object... elements) {
        Assert.notNull(elements, "Elements must not be null");
        this.params = new Object[elements.length];
        System.arraycopy(elements, 0, this.params, 0, elements.length);
        this.hashCode = Arrays.deepHashCode(this.params);
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj || (obj instanceof SimpleKey
                && Arrays.deepEquals(this.params, ((SimpleKey) obj).params)));
    }

    @Override
    public final int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
    }

    public Object[] getParams() {
        return params;
    }
}


package com.scistor.estools.util;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * @author tianlin
 *
 */
public class AssertUtil {
    /**
     * 不为空字符串检测
     * 
     * @param text
     */
    public static void hasTextString(@Nullable String text) {
        Assert.hasText(text, "this argument must have text; it must not be null, empty, or blank");
    }

    /**
     * 不为空数组
     * 
     * @param array
     */
    public static void notEmptyArray(@Nullable Object[] array) {
        Assert.notEmpty(array, "this array must not be empty: it must contain at least 1 element");
    }

    /**
     * 不为空collection
     * 
     * @param collection
     */
    public static void notEmptyCollection(@Nullable Collection<?> collection) {
        Assert.notEmpty(collection, "this collection must not be empty: it must contain at least 1 element");
    }

    /**
     * map不为空
     * @param map
     */
    public static void notEmptyMap(@Nullable Map<?, ?> map) {
        Assert.notEmpty(map, "this map must not be empty: it must contain at least 1 element");
    }

    /**
     * 不为空对象
     * 
     * @param object
     */
    public static void notNullObject(@Nullable Object object) {
        Assert.notNull(object, "this object must not be null");
    }

    public static void isTrue(boolean expression) {
        isTrue(expression, "this expression must be true");
    }

    public static void isTrue(boolean expression, String msg) {
        Assert.isTrue(expression, msg);
    }
}

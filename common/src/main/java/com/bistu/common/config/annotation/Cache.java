package com.bistu.common.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * expire :过期时间，默认30分钟。
 * name:缓存的key name,默认为service+method+params。
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cache {
    long expire() default 30 * 60 * 1000;
    String name() default "";
}

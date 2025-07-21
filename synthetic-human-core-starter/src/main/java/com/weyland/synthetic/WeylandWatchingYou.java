package com.weyland.synthetic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WeylandWatchingYou {
    String author() default "system";
    boolean logParameters() default true;
    boolean logResult() default true;
}
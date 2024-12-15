package com.notice_board.common.annotation;

import com.notice_board.common.component.CommonExceptionResultMessage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorCodeExamples {
    CommonExceptionResultMessage[] value();
}

package com.example.auctionapp.annotation;

import java.lang.annotation.*;

/**
 * @author Administrator
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {

    String value() default "";
}

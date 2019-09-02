package com.example.auctionapp.annotation;


import java.lang.annotation.*;

/**
 * 防对象空注解,value为字符串数组,每个字符串对应类中的每个字段
 *
 * @author 安能
 * @date 2019年7月30日13:24:25
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ObjectNotNull {

    /**
     * 字符串数组,每个字符串对应类中的每个字段
     *
     * @return 防空数组
     */
    String[] value() default "";
}

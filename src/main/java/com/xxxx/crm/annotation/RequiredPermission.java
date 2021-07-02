package com.xxxx.crm.annotation;

import java.lang.annotation.*;

/**
 * @author lms
 * @date 2021-07-02 - 16:54
 * 自定义一个注解，目的用于判断传过来的状态码值，从而判断相应的角色拥有的权限
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {
    // 设置参数名称,默认为空
    String code() default "";
}

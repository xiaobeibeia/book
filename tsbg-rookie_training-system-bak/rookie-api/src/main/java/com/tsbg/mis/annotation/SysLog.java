package com.tsbg.mis.annotation;

import java.lang.annotation.*;

/**
 * 系统操作日志注解
 *
 * @Author: 海波
 * @Date: 2019/12/5 19:34
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {
    String value() default "";

}

package com.orrin.sca.component.utils.json.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author orrin.zhang on 2017/8/4.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(JSONS.class)   // 让方法支持多重@JSON 注解
public @interface JSON {
	Class<?> type();
	String include() default "";
	String filter() default "";
}
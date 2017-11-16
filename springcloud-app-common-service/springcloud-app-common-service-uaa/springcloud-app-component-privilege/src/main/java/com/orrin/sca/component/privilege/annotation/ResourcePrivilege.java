package com.orrin.sca.component.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourcePrivilege {
    boolean anonymous() default false;
    boolean autoAdd2DB() default true;
    String resourceGlobalUniqueId();
    String resourceName() default "";
    String resourceDesc() default "";
}

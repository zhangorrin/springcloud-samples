package com.orrin.sca.component.privilege.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.ManagementServerProperties;
import org.springframework.core.annotation.Order;

@Aspect
@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER + 10)
public class AspectPrivilege {
    private final Logger logger = LoggerFactory.getLogger(AspectPrivilege.class);

    @Before(value = "@annotation(com.orrin.sca.component.privilege.annotation.ResourcePrivilege)")
    public void beforeMethod(JoinPoint jp){
        logger.info("beforeMethod");

        String methodName = jp.getSignature().getName();
        String className = jp.getTarget().getClass().getName();

        logger.info("methodName = {}", methodName);
    }
}

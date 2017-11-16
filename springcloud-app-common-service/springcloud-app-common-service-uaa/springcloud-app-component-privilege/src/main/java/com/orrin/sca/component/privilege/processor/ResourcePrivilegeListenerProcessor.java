package com.orrin.sca.component.privilege.processor;

import com.orrin.sca.common.service.uaa.client.feignclient.SysResourceServiceApi;
import com.orrin.sca.component.privilege.annotation.ResourcePrivilege;
import com.orrin.sca.component.privilege.annotation.ResourcePrivilegeEntity;
import com.orrin.sca.component.utils.spring.SpringUtil;
import com.orrin.sca.framework.core.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.*;

public class ResourcePrivilegeListenerProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ResourcePrivilegeListenerProcessor.class);

    private Map<String, Object> beans;

    @Autowired
    private SysResourceServiceApi sysResourceServiceApi;

    public Map<String, Object> getBeans() {
        return beans;
    }

    public ResourcePrivilegeListenerProcessor() {
        beans = new HashMap<>();
        beans.putAll(SpringUtil.getBeansWithAnnotation(RestController.class));
        beans.putAll(SpringUtil.getBeansWithAnnotation(Controller.class));

        List<ResourcePrivilegeEntity> resourcePrivilegeEntityList = this.getResourcePrivilegeByannotation();
        this.increaseResourcePrivilege2DB(resourcePrivilegeEntityList);
    }

    public void increaseResourcePrivilege2DB(List<ResourcePrivilegeEntity> resourcePrivilegeEntityList) {
        if(resourcePrivilegeEntityList != null && resourcePrivilegeEntityList.size() > 0){
            for(ResourcePrivilegeEntity rpe : resourcePrivilegeEntityList) {
                sysResourceServiceApi.insertPrivilege(rpe);
            }
        }
    }

    public List<ResourcePrivilegeEntity> getResourcePrivilegeByannotation(){
        List<ResourcePrivilegeEntity> list = new ArrayList<>();
        List<Method> methodList = new ArrayList<>();

        for(Object bean:beans.values()){
            Method[] methods = ReflectionUtils.getAllDeclaredMethods(bean.getClass());
            methodList.addAll(Arrays.asList(methods));
        }

        Set<String> globalUniqueIdSet = new HashSet<>();

        for(Method method : methodList) {
            ResourcePrivilege resourcePrivilege = AnnotationUtils.getAnnotation(method, ResourcePrivilege.class);

            RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);

            if(resourcePrivilege != null){
                logger.info("getResourcePrivilegeByannotation resourceGlobalUniqueId = {},resourceName = {}",resourcePrivilege.resourceGlobalUniqueId(), resourcePrivilege.resourceName());
                if(globalUniqueIdSet.contains(resourcePrivilege.resourceGlobalUniqueId())){
                    throw new BusinessException("10001"," globalUniqueId repeate ! : " + "resourcePrivilege.resourceGlobalUniqueId()");
                }
                globalUniqueIdSet.add(resourcePrivilege.resourceGlobalUniqueId());

                ResourcePrivilegeEntity resourcePrivilegeEntity = new ResourcePrivilegeEntity();
                resourcePrivilegeEntity.setGlobalUniqueId(resourcePrivilege.resourceGlobalUniqueId());
                resourcePrivilegeEntity.setResourceName(resourcePrivilege.resourceName());
                resourcePrivilegeEntity.setResourceDesc(resourcePrivilege.resourceDesc());

                if(requestMapping != null) {
                    String paths[] = (requestMapping.path()!=null)?requestMapping.path():requestMapping.value();
                    resourcePrivilegeEntity.setResourcePath(StringUtils.arrayToDelimitedString(paths, ","));
                }

                list.add(resourcePrivilegeEntity);
            }

        }

        return list;
    }

    @Override
    public String toString() {
        for(Object bean:beans.values()){
            System.err.println(bean==null?"null":bean.getClass().getName());
        }
        System.err.println("=====ContextRefreshedEvent=====");

        return "ResourcePrivilegeListenerProcessor{" +
                "beans=" + beans +
                '}';
    }
}

package com.orrin.sca.component.privilege.intercept;

import com.orrin.sca.common.service.uaa.client.feignclient.SysResourceServiceApi;
import com.orrin.sca.common.service.uaa.client.vo.ResourceBriefInfo;
import com.orrin.sca.component.privilege.annotation.ResourcePrivilege;
import com.orrin.sca.component.privilege.model.RequestAuthForMatcher;
import com.orrin.sca.component.redis.config.Prefixes;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component("urlFilterInvocationAuthority")
public class URLFilterInvocationAuthority implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(URLFilterInvocationAuthority.class);
    private final static List<ConfigAttribute> NULL_CONFIG_ATTRIBUTE = Collections.emptyList();

    @Resource(name= "redisTemplate")
    private HashOperations<String,String,RequestAuthForMatcher> opsForHash;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SysResourceServiceApi sysResourceServiceApi;

    private List<HandlerMapping> handlerMappings;

    @Override
    public void afterPropertiesSet() throws Exception {
        //this.loadResuorce();
        logger.info("资源权限列表 init finished");
    }

    public void refreshResuorceAttributes(){
        this.loadResuorce();
    }

    public Collection<ConfigAttribute> getAttributes(String matchRootPath, final HttpServletRequest request) throws IllegalArgumentException {
        long startTime = System.currentTimeMillis();

        Collection<ConfigAttribute> attrs = NULL_CONFIG_ATTRIBUTE;
        List<RequestAuthForMatcher> requestMatchers = new ArrayList<>();

        Map<String, RequestAuthForMatcher> resourceAndAuth = opsForHash.entries(Prefixes.RESOURCE_AND_AUTHORITIES_SET.getValue());
        if(resourceAndAuth == null || resourceAndAuth.size() == 0) {
            this.loadResuorce();
            resourceAndAuth = opsForHash.entries(Prefixes.RESOURCE_AND_AUTHORITIES_SET.getValue());
        }

        Set<String> reallyRequestPathSet = new HashSet<>();

        if(resourceAndAuth != null){

			/*for(Map.Entry<String, RequestAuthForMatcher> entry : resourceAndAuth.entrySet() ) {
				Collection<ConfigAttribute> atts = SecurityConfig.createListFromCommaDelimitedString(entry.getValue().getAuthorityMarks());
				RequestMatcher requestMatcher = new AntPathRequestMatcher(entry.getValue().getResourcePath(),entry.getValue().getRequestMethod().toUpperCase(),true);
				if (requestMatcher.matches(request)) {
					attrs.addAll(atts);
				}
			}*/

            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
            Map<String, HandlerMapping> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(webApplicationContext, HandlerMapping.class, true, false);
            this.handlerMappings = new ArrayList(matchingBeans.values());

            HandlerExecutionChain mappedHandler = null;
            try {
                mappedHandler = getHandler(request);
                if(mappedHandler.getHandler() instanceof HandlerMethod) {
                    HandlerMethod handlerMethod = (HandlerMethod)mappedHandler.getHandler();

                    RequestMapping classRequestMapping = handlerMethod.getBeanType().getAnnotation(RequestMapping.class);

                    ResourcePrivilege methodResourcePrivilege = handlerMethod.getMethodAnnotation(ResourcePrivilege.class);
                    RequestMapping methodRequestMapping = handlerMethod.getMethodAnnotation(RequestMapping.class);
                    String[] methodPaths = null;
                    String[] classPaths = (classRequestMapping.path().length == 0)? classRequestMapping.value() : classRequestMapping.path();

                    if(methodResourcePrivilege != null){
                        RequestAuthForMatcher requestAuthForMatcher = opsForHash.get(Prefixes.RESOURCE_AND_AUTHORITIES_SET.getValue(),methodResourcePrivilege.resourceGlobalUniqueId());
                        attrs = SecurityConfig.createListFromCommaDelimitedString(requestAuthForMatcher.getAuthorityMarks());

                        logger.info("URL资源match："+request.getRequestURI()+ " -> " + attrs);
                        logger.info("URL资源match execute TimeMillis = {} ", (System.currentTimeMillis() - startTime));
                        return attrs;
                    }else if(methodRequestMapping!=null) {
                        methodPaths = methodRequestMapping.path().length == 0 ? methodRequestMapping.value() : methodRequestMapping.path();
                        if( methodPaths != null){
                            for(String methodPath : methodPaths){
                                String wholePath = methodPath.startsWith("/") ? methodPath : ("/" +methodPath);
                                if(classPaths != null){
                                    for(String classPath : classPaths) {
                                        wholePath = (classPath.endsWith("/") ? classPath.substring(0,classPath.length()-1) : classPath) + wholePath;
                                        logger.info("wholePath = {}", wholePath);
                                        reallyRequestPathSet.add(wholePath);
                                    }
                                }else {
                                    logger.info("wholePath = {}", wholePath);
                                    reallyRequestPathSet.add(wholePath);
                                }
                            }
                        }
                    }
                }else if(mappedHandler.getHandler() instanceof ZuulController) {

                }
                for(Map.Entry<String, RequestAuthForMatcher> entry : resourceAndAuth.entrySet() ) {
                    //Collection<ConfigAttribute> atts = SecurityConfig.createListFromCommaDelimitedString(entry.getValue().getAuthorityMarks());
                    String path = matchRootPath + "/"+ entry.getValue().getResourcePath();
                    if(path.contains("//")) {
                        path = path.replaceAll("//","/");
                    }
                    RequestMatcher requestMatcher = new AntPathRequestMatcher(path,entry.getValue().getRequestMethod().toUpperCase(),true);
                    if (requestMatcher.matches(request)) {
                        requestMatchers.add(entry.getValue());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(requestMatchers != null && requestMatchers.size() == 1){
            attrs = SecurityConfig.createListFromCommaDelimitedString(requestMatchers.get(0).getAuthorityMarks());
        }else if(requestMatchers != null && requestMatchers.size() > 0 && reallyRequestPathSet.size() > 0){
            for(String reallyRequestPath : reallyRequestPathSet) {
                for(RequestAuthForMatcher rafm : requestMatchers) {
                    if(rafm.getResourcePath().equalsIgnoreCase(reallyRequestPath)) {
                        attrs = SecurityConfig.createListFromCommaDelimitedString(rafm.getAuthorityMarks());
                        break;
                    }
                }
                if(attrs != null && attrs.size() > 0){
                    break;
                }
            }
        }

        if(attrs == null || attrs.size() == 0){
            attrs = SecurityConfig.createListFromCommaDelimitedString("permitAll");
        }

        logger.info("URL资源match："+request.getRequestURI()+ " -> " + attrs);
        logger.info("URL资源match execute TimeMillis = {} ", (System.currentTimeMillis() - startTime));
        return attrs;
    }

    public Set<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

        Map<String, RequestAuthForMatcher> resourceAndAuth = opsForHash.entries(Prefixes.RESOURCE_AND_AUTHORITIES_SET.getValue());
        if(resourceAndAuth == null && resourceAndAuth.size() == 0) {
            this.loadResuorce();
            resourceAndAuth = opsForHash.entries(Prefixes.RESOURCE_AND_AUTHORITIES_SET.getValue());
        }

        if(resourceAndAuth != null){
            for(Map.Entry<String, RequestAuthForMatcher> entry : resourceAndAuth.entrySet() ) {
                Collection<ConfigAttribute> atts = SecurityConfig.createListFromCommaDelimitedString(entry.getValue().getAuthorityMarks());
                allAttributes.addAll(atts);

            }
        }

        return allAttributes;
    }

    /**
     * 获取到url地址和AUTH_**这种权限标识，注意：不是权限ID和资源ID
     * @return
     */
    private List<ResourceBriefInfo> getURLResourceMapping(){
        List<ResourceBriefInfo> list = sysResourceServiceApi.findAuthResources();

        return list;
    }

    private Map<String,RequestAuthForMatcher> loadResuorce(){

        Map<String,RequestAuthForMatcher> map = new LinkedHashMap<>();

        List<ResourceBriefInfo> list = getURLResourceMapping();

        if(list != null){
            for(ResourceBriefInfo rbi : list) {
                String resourcePath = rbi.getResourcePath().startsWith("/") ? rbi.getResourcePath() : ("/" +rbi.getResourcePath());

                if(map.containsKey(rbi.getGlobalUniqueId())){
                    RequestAuthForMatcher mark = map.get(resourcePath);
                    mark.setAuthorityMarks(mark.getAuthorityMarks()+","+rbi.getAuthorityMark());
                    map.put(rbi.getGlobalUniqueId(), mark);
                }else{
                    RequestAuthForMatcher mark = new RequestAuthForMatcher();
                    mark.setAuthorityMarks(rbi.getAuthorityMark());
                    mark.setGlobalUniqueId(rbi.getGlobalUniqueId());
                    mark.setRequestMethod(rbi.getRequestMethod());
                    mark.setResourcePath(resourcePath);
                    map.put(rbi.getGlobalUniqueId(), mark);
                }
            }
        }

        RLock lock = redissonClient.getLock(Prefixes.RESOURCE_AND_AUTHORITIES_LOCK.getValue());
        try {
            long threadId = Thread.currentThread().getId();
            boolean res = lock.tryLock(10, TimeUnit.SECONDS);
            System.out.println(threadId + ":"+res);
            if(res) {
                opsForHash.putAll(Prefixes.RESOURCE_AND_AUTHORITIES_SET.getValue(), map);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

        return map;
    }

    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        Iterator var2 = this.handlerMappings.iterator();

        HandlerExecutionChain handler;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            HandlerMapping hm = (HandlerMapping)var2.next();
            if (logger.isTraceEnabled()) {
                logger.trace("Testing handler map [" + hm + "] in DispatcherServlet with name '" + this.getClass().getName() + "'");
            }

            handler = hm.getHandler(request);
        } while(handler == null);

        return handler;
    }

}

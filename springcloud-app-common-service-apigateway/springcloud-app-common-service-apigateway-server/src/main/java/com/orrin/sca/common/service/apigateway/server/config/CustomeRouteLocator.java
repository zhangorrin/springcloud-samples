package com.orrin.sca.common.service.apigateway.server.config;

import com.orrin.sca.common.service.apigateway.client.domain.ZuulRouteEntity;
import com.orrin.sca.common.service.apigateway.server.service.impl.ZuulRouteServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.cloud.netflix.zuul.filters.discovery.ServiceRouteMapper;
import org.springframework.cloud.netflix.zuul.filters.discovery.SimpleServiceRouteMapper;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class CustomeRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomeRouteLocator.class);

    protected ZuulProperties properties;
    private DiscoveryClient discovery;
    private ServiceRouteMapper serviceRouteMapper;
    private ZuulRouteServiceImpl zuulRouteService;

    public CustomeRouteLocator(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
        this.properties = properties;
        this.serviceRouteMapper = new SimpleServiceRouteMapper();
        LOGGER.info("servletPath = {}", servletPath);
    }

    public CustomeRouteLocator(String servletPath, ZuulProperties properties,
                               DiscoveryClient discovery, ZuulRouteServiceImpl zuulRouteService) {
        super(servletPath, properties);
        this.properties = properties;
        this.discovery = discovery;

        if(this.discovery != null) {
            if (properties.isIgnoreLocalService()) {
                ServiceInstance instance = null;

                try {
                    instance = discovery.getLocalServiceInstance();
                } catch (Exception var6) {
                    LOGGER.warn("Error locating local service instance", var6);
                }

                if (instance != null) {
                    String localServiceId = instance.getServiceId();
                    if (!properties.getIgnoredServices().contains(localServiceId)) {
                        properties.getIgnoredServices().add(localServiceId);
                    }
                }
            }
        }

        this.serviceRouteMapper = new SimpleServiceRouteMapper();
        this.zuulRouteService = zuulRouteService;
        LOGGER.info("servletPath = {}", servletPath);
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap();

        //load from application.properties
        routesMap.putAll(super.locateRoutes());

        //load from db
        if(zuulRouteService != null) {
            routesMap.putAll(locateRoutesFromDB(zuulRouteService.findAll()));
        }

        LinkedHashMap values;
        Iterator var3;
        String path;
        if (this.discovery != null) {
            values = new LinkedHashMap();
            var3 = routesMap.values().iterator();

            while(var3.hasNext()) {
                ZuulRoute route = (ZuulRoute)var3.next();
                path = route.getServiceId();
                if (path == null) {
                    path = route.getId();
                }

                if (path != null) {
                    values.put(path, route);
                }
            }

            List<String> services = this.discovery.getServices();
            String[] ignored = (String[])this.properties.getIgnoredServices().toArray(new String[0]);
            Iterator var13 = services.iterator();

            while(var13.hasNext()) {
                String serviceId = (String)var13.next();
                String key = "/" + this.mapRouteToService(serviceId) + "/**";
                if (values.containsKey(serviceId) && ((ZuulRoute)values.get(serviceId)).getUrl() == null) {
                    ZuulRoute staticRoute = (ZuulRoute)values.get(serviceId);
                    if (!StringUtils.hasText(staticRoute.getLocation())) {
                        staticRoute.setLocation(serviceId);
                    }
                }

                if (!PatternMatchUtils.simpleMatch(ignored, serviceId) && !routesMap.containsKey(key)) {
                    routesMap.put(key, new ZuulRoute(key, serviceId));
                }
            }
        }

        if (routesMap.get("/**") != null) {
            ZuulRoute defaultRoute = (ZuulRoute)routesMap.get("/**");
            routesMap.remove("/**");
            routesMap.put("/**", defaultRoute);
        }

        values = new LinkedHashMap();

        Map.Entry entry;
        for(var3 = routesMap.entrySet().iterator(); var3.hasNext(); values.put(path, entry.getValue())) {
            entry = (Map.Entry)var3.next();
            path = (String)entry.getKey();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }

            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
        }

        return values;
    }

    public Map<String, ZuulRoute> locateRoutesFromDB(List<ZuulRouteEntity> dbZuulRoutes) {
        Map<String, ZuulRoute> zuulRouteMap = new HashMap<>();

        for(ZuulRouteEntity zre : dbZuulRoutes) {
            ZuulRoute zuulRoute = new ZuulRoute();
            zuulRoute.setId(zre.getZuulRouteId());
            zuulRoute.setPath(zre.getPath());
            zuulRoute.setServiceId(zre.getServiceId());
            zuulRoute.setUrl(zre.getUrl());
            zuulRoute.setStripPrefix(zre.getStripPrefix());
            zuulRoute.setRetryable(zre.getRetryable());
            if(StringUtils.hasText(zre.getSensitiveHeaders())){
                zuulRoute.setSensitiveHeaders(new LinkedHashSet(Arrays.asList(zre.getSensitiveHeaders().split(","))));
            }
            zuulRoute.setCustomSensitiveHeaders(zre.getCustomeSensitiveHeaders());
            zuulRouteMap.put(zuulRoute.getPath(), zuulRoute);
        }
        return zuulRouteMap;
    }

    protected String mapRouteToService(String serviceId) {
        return this.serviceRouteMapper.apply(serviceId);
    }
}

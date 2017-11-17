package com.orrin.sca.common.service.apigateway.client.feignclient;

import com.orrin.sca.common.service.apigateway.client.domain.ZuulRouteEntity;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "common-service-apigateway")
public interface ZuulRouteServiceApi {

    @RequestMapping(path = "/api/zuul_route/{zuulRoutId}", method = RequestMethod.GET)
    ResponseResult<ZuulRouteEntity> getZuulRouteById(@PathVariable("zuulRoutId") String zuulRoutId);

    @RequestMapping(path = "/api/zuul_route", method = RequestMethod.GET)
    ResponseResult<List<ZuulRouteEntity>> getAllZuulRoute();
}

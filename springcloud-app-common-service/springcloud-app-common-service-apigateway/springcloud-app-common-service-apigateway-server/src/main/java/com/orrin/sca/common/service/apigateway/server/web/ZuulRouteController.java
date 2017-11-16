package com.orrin.sca.common.service.apigateway.server.web;

import com.orrin.sca.common.service.apigateway.client.domain.ZuulRouteEntity;
import com.orrin.sca.common.service.apigateway.client.feignclient.ZuulRouteServiceApi;
import com.orrin.sca.common.service.apigateway.server.dao.ZuulRouteRepository;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/zuul_route")
public class ZuulRouteController implements ZuulRouteServiceApi{

    @Autowired
    private ZuulRouteRepository zuulRouteRepository;

    @Override
    @RequestMapping(path = "/{zuulRoutId}", method = RequestMethod.GET)
    public ResponseResult<ZuulRouteEntity> getZuulRouteById(@PathVariable("zuulRoutId") String zuulRoutId) {
        ResponseResult<ZuulRouteEntity> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setData(zuulRouteRepository.findOne(zuulRoutId));
        return responseResult;
    }

    @Override
    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseResult<List<ZuulRouteEntity>> getAllZuulRoute() {
        ResponseResult<List<ZuulRouteEntity>> responseResult = new ResponseResult<>();
        responseResult.setResponseCode("00000");
        responseResult.setData(zuulRouteRepository.findAll());
        return responseResult;
    }
}

package com.orrin.sca.common.service.apigateway.server.service.impl;

import com.orrin.sca.common.service.apigateway.client.domain.ZuulRouteEntity;
import com.orrin.sca.common.service.apigateway.server.dao.ZuulRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("zuulRouteService")
public class ZuulRouteServiceImpl {

    @Autowired
    private ZuulRouteRepository zuulRouteRepository;

    @Cacheable(value = "common-service-apigateway:zuul", key = "'dbroutes'")
    public List<ZuulRouteEntity> findAll(){
        return zuulRouteRepository.findAll();
    }

    @CacheEvict(value = "common-service-apigateway:zuul", key = "'dbroutes'")
    @Transactional(rollbackFor = Exception.class)
    public ZuulRouteEntity saveAndFlush(ZuulRouteEntity zuulRouteEntity) {
        return zuulRouteRepository.saveAndFlush(zuulRouteEntity);
    }

    @CacheEvict(value = "common-service-apigateway:zuul", key = "'dbroutes'")
    @Transactional(rollbackFor = Exception.class)
    public void delete(String zuulRouteId) {
        zuulRouteRepository.delete(zuulRouteId);
    }
}

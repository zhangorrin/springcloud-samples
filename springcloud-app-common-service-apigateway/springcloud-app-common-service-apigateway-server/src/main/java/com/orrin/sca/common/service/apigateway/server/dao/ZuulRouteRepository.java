package com.orrin.sca.common.service.apigateway.server.dao;

import com.orrin.sca.common.service.apigateway.client.domain.ZuulRouteEntity;
import com.orrin.sca.component.jpa.dao.BaseJPARepository;
import org.springframework.stereotype.Repository;

/**
 * @author orrin
 */
@Repository("zuulRouteRepository")
public interface ZuulRouteRepository extends BaseJPARepository<ZuulRouteEntity, String> {
}

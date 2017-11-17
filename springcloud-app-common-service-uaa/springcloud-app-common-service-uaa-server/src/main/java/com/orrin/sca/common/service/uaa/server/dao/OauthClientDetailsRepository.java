package com.orrin.sca.common.service.uaa.server.dao;

import com.orrin.sca.common.service.uaa.client.domain.OauthClientDetailsEntity;
import com.orrin.sca.component.jpa.dao.BaseJPARepository;
import org.springframework.stereotype.Repository;

@Repository("oauthClientDetailsRepository")
public interface OauthClientDetailsRepository extends BaseJPARepository<OauthClientDetailsEntity, String> {
}

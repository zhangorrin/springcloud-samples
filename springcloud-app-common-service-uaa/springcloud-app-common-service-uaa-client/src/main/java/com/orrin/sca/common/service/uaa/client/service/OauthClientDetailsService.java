package com.orrin.sca.common.service.uaa.client.service;

import com.orrin.sca.common.service.uaa.client.domain.OauthClientDetailsEntity;
import com.orrin.sca.component.jpa.dao.Range;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OauthClientDetailsService {

    Page<OauthClientDetailsEntity> queryByExampleWithRange(Example example, List<Range<OauthClientDetailsEntity>> ranges, Pageable pageable);

    OauthClientDetailsEntity findOne(String clientId);

    OauthClientDetailsEntity saveAndFlush(OauthClientDetailsEntity oauthClientDetailsEntity);

    void delete(String clientId);
}

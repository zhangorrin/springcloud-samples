package com.orrin.sca.common.service.uaa.server.service.impl;

import com.orrin.sca.common.service.uaa.client.domain.OauthClientDetailsEntity;
import com.orrin.sca.common.service.uaa.client.service.OauthClientDetailsService;
import com.orrin.sca.common.service.uaa.server.dao.OauthClientDetailsRepository;
import com.orrin.sca.component.jpa.dao.Range;
import com.orrin.sca.framework.core.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("oauthClientDetailsService")
public class OauthClientDetailsServiceImpl implements OauthClientDetailsService {

    @Autowired
    private OauthClientDetailsRepository oauthClientDetailsRepository;

    @Override
    public Page<OauthClientDetailsEntity> queryByExampleWithRange(Example example, List<Range<OauthClientDetailsEntity>> ranges, Pageable pageable) {
        return oauthClientDetailsRepository.queryByExampleWithRange(example, ranges, pageable);
    }

    @Override
    public OauthClientDetailsEntity findOne(String clientId) {
        return oauthClientDetailsRepository.findOne(clientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OauthClientDetailsEntity saveAndFlush(OauthClientDetailsEntity oauthClientDetailsEntity) {
        if(!StringUtils.hasText(oauthClientDetailsEntity.getClientId())) {
            throw new BusinessException("10000", " clientId can not be empty ! ");
        }
        return oauthClientDetailsRepository.saveAndFlush(oauthClientDetailsEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String clientId) {
        oauthClientDetailsRepository.delete(clientId);
    }


}

package com.orrin.sca.common.service.uaa.server.core.oauth;

import com.orrin.sca.common.service.uaa.client.domain.OauthClientDetailsEntity;
import com.orrin.sca.common.service.uaa.server.dao.OauthClientDetailsRepository;
import com.orrin.sca.component.utils.json.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service("clientDetailsServiceImpl")
public class ClientDetailsServiceImpl implements ClientDetailsService {


    private static final Logger logger = LoggerFactory.getLogger(ClientDetailsServiceImpl.class);

    @Autowired
    private OauthClientDetailsRepository oauthClientDetailsRepository;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        OauthClientDetailsEntity oauthClientDetailsEntity = oauthClientDetailsRepository.findOne(clientId);

        logger.info("[*] loadClientByClientId = {}, {}", oauthClientDetailsEntity == null? "null": oauthClientDetailsEntity.getClientId(), oauthClientDetailsEntity == null? "null": oauthClientDetailsEntity.getAuthorizedGrantTypes());

        BaseClientDetails baseClientDetails = new BaseClientDetails(oauthClientDetailsEntity.getClientId(),
                oauthClientDetailsEntity.getResourceIds(),
                oauthClientDetailsEntity.getScope(),
                oauthClientDetailsEntity.getAuthorizedGrantTypes(),
                oauthClientDetailsEntity.getAuthorities(),
                oauthClientDetailsEntity.getWebServerRedirectUri());

        if (StringUtils.hasText(oauthClientDetailsEntity.getClientSecret())) {
            baseClientDetails.setClientSecret(oauthClientDetailsEntity.getClientSecret());
        }

        if (StringUtils.hasText(oauthClientDetailsEntity.getAccessTokenValidity())) {
            baseClientDetails.setAccessTokenValiditySeconds(Integer.parseInt(oauthClientDetailsEntity.getAccessTokenValidity()));
        }

        if (StringUtils.hasText(oauthClientDetailsEntity.getRefreshTokenValidity())) {
            baseClientDetails.setRefreshTokenValiditySeconds(Integer.parseInt(oauthClientDetailsEntity.getRefreshTokenValidity()));
        }

        if(StringUtils.hasText(oauthClientDetailsEntity.getAdditionalInformation())) {
            Map<String, Object> additionalInformation = JacksonUtils.decode(oauthClientDetailsEntity.getAdditionalInformation(), HashMap.class);
            baseClientDetails.setAdditionalInformation(additionalInformation);
        }

        if (StringUtils.hasText(oauthClientDetailsEntity.getAutoapprove())) {
            baseClientDetails.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(oauthClientDetailsEntity.getAutoapprove()));
        }
        return baseClientDetails;
    }
}

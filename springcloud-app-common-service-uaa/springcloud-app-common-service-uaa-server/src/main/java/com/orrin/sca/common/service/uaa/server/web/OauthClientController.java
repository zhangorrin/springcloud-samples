package com.orrin.sca.common.service.uaa.server.web;

import com.orrin.sca.common.service.uaa.client.domain.OauthClientDetailsEntity;
import com.orrin.sca.common.service.uaa.client.service.OauthClientDetailsService;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauthclient")
public class OauthClientController {

    @Autowired
    private OauthClientDetailsService oauthClientDetailsService;

    @GetMapping("/list")
    public ResponseResult<Page<OauthClientDetailsEntity>> getList() {
        return null;
    }

    @GetMapping("/{clientId}")
    public ResponseResult<OauthClientDetailsEntity> getOauthClient(@PathVariable("clientId") String clientId) {
        return null;
    }
}

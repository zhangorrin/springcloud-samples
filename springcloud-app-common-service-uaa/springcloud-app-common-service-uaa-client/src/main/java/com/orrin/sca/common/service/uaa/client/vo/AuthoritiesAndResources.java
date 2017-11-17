package com.orrin.sca.common.service.uaa.client.vo;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysResourcesEntity;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public class AuthoritiesAndResources implements Serializable {
    private static final long serialVersionUID = 1L;

    private SysAuthoritiesEntity authority;

    private Page<SysResourcesEntity> resources;

    public SysAuthoritiesEntity getAuthority() {
        return authority;
    }

    public void setAuthority(SysAuthoritiesEntity authority) {
        this.authority = authority;
    }

    public Page<SysResourcesEntity> getResources() {
        return resources;
    }

    public void setResources(Page<SysResourcesEntity> resources) {
        this.resources = resources;
    }
}

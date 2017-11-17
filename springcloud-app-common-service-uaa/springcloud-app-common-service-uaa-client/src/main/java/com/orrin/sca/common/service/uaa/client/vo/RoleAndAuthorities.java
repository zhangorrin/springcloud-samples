package com.orrin.sca.common.service.uaa.client.vo;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysRolesEntity;
import org.springframework.data.domain.Page;

import java.io.Serializable;

public class RoleAndAuthorities implements Serializable {
    private static final long serialVersionUID = 1L;

    private SysRolesEntity role;

    private Page<SysAuthoritiesEntity> authorities;

    public SysRolesEntity getRole() {
        return role;
    }

    public void setRole(SysRolesEntity role) {
        this.role = role;
    }

    public Page<SysAuthoritiesEntity> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Page<SysAuthoritiesEntity> authorities) {
        this.authorities = authorities;
    }
}

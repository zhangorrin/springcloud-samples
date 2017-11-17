package com.orrin.sca.common.service.uaa.client.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orrin.sca.component.params.BasePageParams;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class RoleRequestParams extends BasePageParams {
    private static final long serialVersionUID = 1L;

    private String roleName;
    private String roleDesc;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDateEnd;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Date getCreatedDateStart() {
        return createdDateStart;
    }

    public void setCreatedDateStart(Date createdDateStart) {
        this.createdDateStart = createdDateStart;
    }

    public Date getCreatedDateEnd() {
        return createdDateEnd;
    }

    public void setCreatedDateEnd(Date createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }
}

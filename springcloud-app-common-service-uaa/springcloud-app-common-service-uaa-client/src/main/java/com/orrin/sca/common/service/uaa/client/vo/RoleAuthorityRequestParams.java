package com.orrin.sca.common.service.uaa.client.vo;

import com.orrin.sca.component.params.BasePageParams;

public class RoleAuthorityRequestParams extends BasePageParams {
    private static final long serialVersionUID = 1L;

    private String authorityMark;
    private String authorityName;

    public String getAuthorityMark() {
        return authorityMark;
    }

    public void setAuthorityMark(String authorityMark) {
        this.authorityMark = authorityMark;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }
}

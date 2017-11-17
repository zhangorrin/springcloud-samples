package com.orrin.sca.common.service.uaa.client.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orrin.sca.component.params.BasePageParams;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class AuthoritiesRequestParams extends BasePageParams {
    private static final long serialVersionUID = 1L;

    private String authorityMark;
    private String authorityName;
    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDateEnd;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

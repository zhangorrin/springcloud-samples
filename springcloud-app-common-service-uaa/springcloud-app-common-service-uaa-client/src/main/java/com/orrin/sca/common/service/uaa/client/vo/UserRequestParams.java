package com.orrin.sca.common.service.uaa.client.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orrin.sca.component.params.BasePageParams;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UserRequestParams extends BasePageParams {
    private static final long serialVersionUID = 1L;
    @JsonProperty(value = "userName")
    private String username;
    private String name;
    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDateStart;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdDateEnd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

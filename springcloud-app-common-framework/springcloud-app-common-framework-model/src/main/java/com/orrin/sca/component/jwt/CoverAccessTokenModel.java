package com.orrin.sca.component.jwt;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class CoverAccessTokenModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("exp")
    private Long exp;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("authorities")
    private List<String> authorities;

    @JsonProperty("jti")
    private String jti;

    @JsonProperty("client_id")
    private String clientId;

    @JsonProperty("scope")
    private List<String> scope;

    public Long getExp() {
        return exp;
    }

    public void setExp(Long exp) {
        this.exp = exp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public List<String> getScope() {
        return scope;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }
}

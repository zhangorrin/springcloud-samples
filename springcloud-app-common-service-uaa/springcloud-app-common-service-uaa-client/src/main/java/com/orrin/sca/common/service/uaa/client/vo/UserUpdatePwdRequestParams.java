package com.orrin.sca.common.service.uaa.client.vo;

import java.io.Serializable;

public class UserUpdatePwdRequestParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String passwd;
    private String passwdCheck;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswdCheck() {
        return passwdCheck;
    }

    public void setPasswdCheck(String passwdCheck) {
        this.passwdCheck = passwdCheck;
    }
}

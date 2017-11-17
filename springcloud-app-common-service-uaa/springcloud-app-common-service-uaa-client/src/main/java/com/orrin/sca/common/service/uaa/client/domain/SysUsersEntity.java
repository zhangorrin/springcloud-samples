package com.orrin.sca.common.service.uaa.client.domain;

import com.orrin.sca.component.jpa.model.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "SYS_USERS")
public class SysUsersEntity extends AbstractAuditingEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "USER_ID")
  private String userId;

  @Column(name = "mobile")
  private String mobile;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "NAME")
  private String name;

  @Column(name = "PASSWORD")
  private String password;

  @Column(name = "LAST_LOGIN")
  private Date lastLogin;

  @Column(name = "DEADLINE")
  private Date deadline;

  @Column(name = "LOGIN_IP")
  private String loginIp;

  @Column(name = "V_QZJGID")
  private String vqzjgid;

  @Column(name = "V_QZJGMC")
  private String vqzjgmc;

  @Column(name = "DEP_ID")
  private String depId;

  @Column(name = "DEP_NAME")
  private String depName;

  @Column(name = "ENABLED")
  private Boolean enabled;

  @Column(name = "ACCOUNT_NON_EXPIRED")
  private Boolean accountNonExpired;

  @Column(name = "ACCOUNT_NON_LOCKED")
  private Boolean accountNonLocked;

  @Column(name = "CREDENTIALS_NON_EXPIRED")
  private Boolean credentialsNonExpired;

  public SysUsersEntity() {
  }

  public SysUsersEntity( boolean initCreateAndUpdateTime) {
    if(!initCreateAndUpdateTime){
      super.setCreatedDate(null);
      super.setLastModifiedDate(null);
    }
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Date lastLogin) {
    this.lastLogin = lastLogin;
  }

  public Date getDeadline() {
    return deadline;
  }

  public void setDeadline(Date deadline) {
    this.deadline = deadline;
  }

  public String getLoginIp() {
    return loginIp;
  }

  public void setLoginIp(String loginIp) {
    this.loginIp = loginIp;
  }

  public String getVqzjgid() {
    return vqzjgid;
  }

  public void setVqzjgid(String vqzjgid) {
    this.vqzjgid = vqzjgid;
  }

  public String getVqzjgmc() {
    return vqzjgmc;
  }

  public void setVqzjgmc(String vqzjgmc) {
    this.vqzjgmc = vqzjgmc;
  }

  public String getDepId() {
    return depId;
  }

  public void setDepId(String depId) {
    this.depId = depId;
  }

  public String getDepName() {
    return depName;
  }

  public void setDepName(String depName) {
    this.depName = depName;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Boolean getAccountNonExpired() {
    return accountNonExpired;
  }

  public void setAccountNonExpired(Boolean accountNonExpired) {
    this.accountNonExpired = accountNonExpired;
  }

  public Boolean getAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(Boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  public Boolean getCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
    this.credentialsNonExpired = credentialsNonExpired;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
}


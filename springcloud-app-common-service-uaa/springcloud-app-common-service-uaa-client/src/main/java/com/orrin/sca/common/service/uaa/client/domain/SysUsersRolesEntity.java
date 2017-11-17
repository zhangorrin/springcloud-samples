package com.orrin.sca.common.service.uaa.client.domain;

import com.orrin.sca.component.jpa.model.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_USERS_ROLES")
public class SysUsersRolesEntity extends AbstractAuditingEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "ROLE_ID")
  private String roleId;

  @Column(name = "USER_ID")
  private String userId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}

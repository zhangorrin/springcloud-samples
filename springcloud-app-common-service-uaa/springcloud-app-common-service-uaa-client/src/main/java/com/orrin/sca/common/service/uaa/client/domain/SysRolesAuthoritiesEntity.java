package com.orrin.sca.common.service.uaa.client.domain;

import com.orrin.sca.component.jpa.model.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_ROLES_AUTHORITIES")
public class SysRolesAuthoritiesEntity extends AbstractAuditingEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "AUTHORITY_ID")
  private String authorityId;

  @Column(name = "ROLE_ID")
  private String roleId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAuthorityId() {
    return authorityId;
  }

  public void setAuthorityId(String authorityId) {
    this.authorityId = authorityId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }
}

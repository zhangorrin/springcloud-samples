package com.orrin.sca.common.service.uaa.client.domain;

import com.orrin.sca.component.jpa.model.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_ROLES_MOUDLES")
public class SysRolesMoudlesEntity extends AbstractAuditingEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  private String id;

  @Column(name = "MODULE_ID")
  private String moduleId;

  @Column(name = "ROLE_ID")
  private String roleId;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getModuleId() {
    return moduleId;
  }

  public void setModuleId(String moduleId) {
    this.moduleId = moduleId;
  }

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }
}

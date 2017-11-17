package com.orrin.sca.common.service.uaa.client.domain;

import com.orrin.sca.component.jpa.model.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_ROLES")
public class SysRolesEntity extends AbstractAuditingEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "ROLE_ID")
  private String roleId;

  @Column(name = "ROLE_NAME")
  private String roleName;

  @Column(name = "ROLE_DESC")
  private String roleDesc;

  @Column(name = "ENABLE")
  private Boolean enable;

  @Column(name = "ISSYS")
  private Boolean issys;

  @Column(name = "MODULE_ID")
  private String moduleId;

  public String getRoleId() {
    return roleId;
  }

  public void setRoleId(String roleId) {
    this.roleId = roleId;
  }

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

  public Boolean getEnable() {
    return enable;
  }

  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  public Boolean getIssys() {
    return issys;
  }

  public void setIssys(Boolean issys) {
    this.issys = issys;
  }

  public String getModuleId() {
    return moduleId;
  }

  public void setModuleId(String moduleId) {
    this.moduleId = moduleId;
  }
}

package com.orrin.sca.common.service.uaa.client.domain;

import com.orrin.sca.component.jpa.model.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYS_AUTHORITIES")
public class SysAuthoritiesEntity extends AbstractAuditingEntity {

  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "authority_id")
  private String authorityId;

  @Column(name = "authority_mark")
  private String authorityMark;

  @Column(name = "authority_name")
  private String authorityName;

  @Column(name = "authority_desc")
  private String authorityDesc;

  @Column(name = "message")
  private String message;

  @Column(name = "enable")
  private Boolean enable;

  @Column(name = "issys")
  private Boolean issys;

  @Column(name = "moduleId")
  private String moduleId;

  public String getAuthorityId() {
    return authorityId;
  }

  public void setAuthorityId(String authorityId) {
    this.authorityId = authorityId;
  }

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

  public String getAuthorityDesc() {
    return authorityDesc;
  }

  public void setAuthorityDesc(String authorityDesc) {
    this.authorityDesc = authorityDesc;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
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

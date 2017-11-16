package com.orrin.sca.component.jpa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * @author orrin.zhang on 2017/8/3.
 */
@MappedSuperclass
@Audited
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements AbstractAuditingInterface, Serializable {

	private static final long serialVersionUID = 1L;

	public AbstractAuditingEntity() {
	}

	public AbstractAuditingEntity(String createdBy, Date createdDate, String lastModifiedBy, Date lastModifiedDate) {
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	@CreatedBy
	@Column(name = "created_by", nullable = false, length = 50, updatable = false)
	private String createdBy;

	@CreatedDate
	@Column(name = "created_date", nullable = false, updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdDate;

	/**
	 @CreatedDate
	 @Column(name = "created_date", nullable = false, updatable = false)
	 @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	 @JsonDeserialize(using=DateTimeDesrializer.class)
	 @JsonSerialize(using = DateTimeSerializer.class)
	 private DateTime createdDate;
	 */

	@LastModifiedBy
	@Column(name = "last_modified_by", length = 50)
	private String lastModifiedBy;

	@LastModifiedDate
	@Column(name = "last_modified_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastModifiedDate;

	/**
	 @LastModifiedDate
	 @Column(name = "last_modified_date")
	 @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	 @JsonDeserialize(using=DateTimeDesrializer.class)
	 @JsonSerialize(using = DateTimeSerializer.class)
	 private DateTime lastModifiedDate;
	 */

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
}

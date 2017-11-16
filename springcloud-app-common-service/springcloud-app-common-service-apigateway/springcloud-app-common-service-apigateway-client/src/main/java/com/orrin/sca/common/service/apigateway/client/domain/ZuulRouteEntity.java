package com.orrin.sca.common.service.apigateway.client.domain;

import com.orrin.sca.component.jpa.model.AbstractAuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "zuul_route")
public class ZuulRouteEntity extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "zuul_route_id", nullable = false, length = 100)
    private String zuulRouteId;

    @Column(name = "path")
    private String  path;

    @Column(name = "service_id")
    private String serviceId;

    @Column(name = "url")
    private String  url;

    @Column(name = "strip_prefix")
    private Boolean stripPrefix;

    @Column(name = "retryable")
    private Boolean retryable;

    @Column(name = "sensitive_headers")
    private String  sensitiveHeaders;


    @Column(name = "custome_sensitive_headers")
    private Boolean  customeSensitiveHeaders;

    @Column(name = "service_name")
    private String serviceName;

    public String getZuulRouteId() {
        return zuulRouteId;
    }

    public void setZuulRouteId(String zuulRouteId) {
        this.zuulRouteId = zuulRouteId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getStripPrefix() {
        return stripPrefix;
    }

    public void setStripPrefix(Boolean stripPrefix) {
        this.stripPrefix = stripPrefix;
    }

    public Boolean getRetryable() {
        return retryable;
    }

    public void setRetryable(Boolean retryable) {
        this.retryable = retryable;
    }

    public String getSensitiveHeaders() {
        return sensitiveHeaders;
    }

    public void setSensitiveHeaders(String sensitiveHeaders) {
        this.sensitiveHeaders = sensitiveHeaders;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Boolean getCustomeSensitiveHeaders() {
        return customeSensitiveHeaders;
    }

    public void setCustomeSensitiveHeaders(Boolean customeSensitiveHeaders) {
        this.customeSensitiveHeaders = customeSensitiveHeaders;
    }
}

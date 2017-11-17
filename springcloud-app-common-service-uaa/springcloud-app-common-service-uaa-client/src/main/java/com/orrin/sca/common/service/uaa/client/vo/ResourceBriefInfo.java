package com.orrin.sca.common.service.uaa.client.vo;


import java.io.Serializable;

public class ResourceBriefInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String resourcePath;
    private String authorityMark;
    private String globalUniqueId;
    private String requestMethod;

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getAuthorityMark() {
        return authorityMark;
    }

    public void setAuthorityMark(String authorityMark) {
        this.authorityMark = authorityMark;
    }

    public String getGlobalUniqueId() {
        return globalUniqueId;
    }

    public void setGlobalUniqueId(String globalUniqueId) {
        this.globalUniqueId = globalUniqueId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }
}

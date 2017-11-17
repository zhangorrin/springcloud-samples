package com.orrin.sca.component.privilege.model;

import java.io.Serializable;

public class RequestAuthForMatcher implements Serializable {
    private static final long serialVersionUID = 1L;

    private String resourcePath;
    private String authorityMarks;
    private String globalUniqueId;
    private String requestMethod;

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getAuthorityMarks() {
        return authorityMarks;
    }

    public void setAuthorityMarks(String authorityMarks) {
        this.authorityMarks = authorityMarks;
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

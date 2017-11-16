package com.orrin.sca.component.privilege.annotation;

import java.io.Serializable;

public class ResourcePrivilegeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String resourceName;

    private String resourceDesc;

    private String resourcePath;

    private String globalUniqueId;

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public String getGlobalUniqueId() {
        return globalUniqueId;
    }

    public void setGlobalUniqueId(String globalUniqueId) {
        this.globalUniqueId = globalUniqueId;
    }
}

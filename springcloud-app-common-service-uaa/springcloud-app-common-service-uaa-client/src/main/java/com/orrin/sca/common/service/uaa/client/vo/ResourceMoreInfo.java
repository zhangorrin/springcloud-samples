package com.orrin.sca.common.service.uaa.client.vo;


import com.orrin.sca.common.service.uaa.client.domain.SysResourcesEntity;

import java.io.Serializable;
import java.util.List;

public class ResourceMoreInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private SysResourcesEntity mainResource;

    private List<SysResourcesEntity> contentResources;

    public SysResourcesEntity getMainResource() {
        return mainResource;
    }

    public void setMainResource(SysResourcesEntity mainResource) {
        this.mainResource = mainResource;
    }

    public List<SysResourcesEntity> getContentResources() {
        return contentResources;
    }

    public void setContentResources(List<SysResourcesEntity> contentResources) {
        this.contentResources = contentResources;
    }
}

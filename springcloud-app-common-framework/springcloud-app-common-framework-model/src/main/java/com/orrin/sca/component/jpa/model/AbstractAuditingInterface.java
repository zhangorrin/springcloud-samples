package com.orrin.sca.component.jpa.model;

import java.io.Serializable;
import java.util.Date;

public interface AbstractAuditingInterface extends Serializable {
    public String getCreatedBy();

    public Date getCreatedDate();

    public String getLastModifiedBy();

    public Date getLastModifiedDate();

}

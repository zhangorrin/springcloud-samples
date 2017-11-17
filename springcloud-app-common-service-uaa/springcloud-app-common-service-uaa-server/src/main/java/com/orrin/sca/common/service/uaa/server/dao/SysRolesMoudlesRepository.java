package com.orrin.sca.common.service.uaa.server.dao;

import com.orrin.sca.common.service.uaa.client.domain.SysRolesMoudlesEntity;
import com.orrin.sca.component.jpa.dao.BaseJPARepository;
import org.springframework.stereotype.Repository;

/**
 * @author Orrin on 2017/7/8.
 */
@Repository("sysRolesMoudlesRepository")
public interface SysRolesMoudlesRepository extends BaseJPARepository<SysRolesMoudlesEntity, String> {

}

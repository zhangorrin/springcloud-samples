package com.orrin.sca.common.service.uaa.server.dao;

import com.orrin.sca.common.service.uaa.client.domain.SysRolesEntity;
import com.orrin.sca.component.jpa.dao.BaseJPARepository;
import org.springframework.stereotype.Repository;

/**
 * @author Orrin on 2017/7/8.
 */
@Repository("sysRolesRepository")
public interface SysRolesRepository extends BaseJPARepository<SysRolesEntity, String> {
    SysRolesEntity findByRoleName(String roleName);
}

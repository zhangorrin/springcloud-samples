package com.orrin.sca.common.service.uaa.client.service;

import com.orrin.sca.common.service.uaa.client.domain.SysRolesMoudlesEntity;
import org.springframework.data.domain.Page;

/**
 * @author Orrin on 2017/7/8.
 */
public interface SysRolesMoudlesService {
	Page<SysRolesMoudlesEntity> findNoCriteria(Integer page, Integer size);
	//Page<SysRolesMoudlesEntity> findCriteria(Integer page, Integer size, SysRolesMoudlesEntity sysRolesMoudles);
}

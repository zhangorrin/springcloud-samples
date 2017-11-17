package com.orrin.sca.common.service.uaa.client.service;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesResourcesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysResourcesEntity;
import com.orrin.sca.common.service.uaa.client.vo.AuthoritiesAndResources;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
public interface SysAuthoritiesResourcesService {
	Page<SysAuthoritiesResourcesEntity> findNoCriteria(Integer page, Integer size);
	//Page<SysAuthoritiesResourcesEntity> findCriteria(Integer page, Integer size, SysAuthoritiesResourcesEntity sysAuthoritiesResources);

	ResponseResult<AuthoritiesAndResources> findAuthoritiesAndResources(String authorityId, String resourceName, Integer page, Integer size);

	Page<SysResourcesEntity> findResourcesUnderAuthority(String authorityId, String resourceName, Integer page, Integer size);

	void deleteByAuthorityId(String authorityId);

	long countByAuthorityId(String authorityId);

	void deleteByAuthorityIdAndResourceId(String authorityId, String resourceId);

	Page<SysResourcesEntity> findResourcesNotUnderAuthoritiy(String authorityId, String resourceName, Integer page, Integer size);

	void addResourcesUnderAuthoritiy(String authorityId, List<SysResourcesEntity> resources);
}

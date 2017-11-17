package com.orrin.sca.common.service.uaa.client.service;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysRolesAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.vo.RoleAndAuthorities;
import com.orrin.sca.common.service.uaa.client.vo.RoleAuthorityRequestParams;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
public interface SysRolesAuthoritiesService {
	Page<SysRolesAuthoritiesEntity> findNoCriteria(Integer page, Integer size);
	//Page<SysRolesAuthoritiesEntity> findCriteria(Integer page, Integer size, SysRolesAuthoritiesEntity sysRolesAuthorities);

	long countByRoleId(String roleId);

	SysRolesAuthoritiesEntity findByAuthorityIdAndRoleId(String authorityId, String roleId);

	void deleteByAuthorityIdAndRoleId(String authorityId, String roleId);

	ResponseResult<RoleAndAuthorities> findRoleAndAuthorities(String roleId, RoleAuthorityRequestParams roleAuthorityRequestParams);

	Page<SysAuthoritiesEntity> findAuthoritiesUnderRole(String roleId, String authorityMark, String authorityName, Pageable pageable);

	Page<SysAuthoritiesEntity> findAuthoritiesNotUnderRole(String roleId, RoleAuthorityRequestParams roleAuthorityRequestParams);


	ResponseResult<Void> addAuthoritiesUnderRole(String roleId, List<SysAuthoritiesEntity> authorities);
}

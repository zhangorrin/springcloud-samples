package com.orrin.sca.common.service.uaa.client.service;

import com.orrin.sca.common.service.uaa.client.domain.SysRolesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysUsersRolesEntity;
import com.orrin.sca.common.service.uaa.client.vo.RoleRequestParams;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
public interface SysUsersRolesService {
	Page<SysUsersRolesEntity> findNoCriteria(Integer page, Integer size);
	//Page<SysUsersRolesEntity> findCriteria(Integer page, Integer size, SysUsersRolesEntity sysUsersRoles);

	List<SysUsersRolesEntity> findDistinctByUserId(String userId);

	ResponseResult<Page<SysRolesEntity>> findRolesUnderUser(String userId, RoleRequestParams roleRequestParams);

	ResponseResult<Page<SysRolesEntity>> findRolesNotUnderUser(String userId, RoleRequestParams roleRequestParams);

	ResponseResult<Void> deleteRoleUnderUser(String userId, String roleId);

	ResponseResult<Void> addRolesUnderUser(String userId, List<SysRolesEntity> roles);

	void deleteByUserId(String userId);
}

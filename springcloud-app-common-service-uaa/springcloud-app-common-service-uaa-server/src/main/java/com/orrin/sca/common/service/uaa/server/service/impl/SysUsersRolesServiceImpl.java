package com.orrin.sca.common.service.uaa.server.service.impl;

import com.orrin.sca.common.service.uaa.client.domain.SysRolesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysUsersEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysUsersRolesEntity;
import com.orrin.sca.common.service.uaa.client.service.SysRolesService;
import com.orrin.sca.common.service.uaa.client.service.SysUsersRolesService;
import com.orrin.sca.common.service.uaa.client.service.SysUsersService;
import com.orrin.sca.common.service.uaa.client.vo.RoleRequestParams;
import com.orrin.sca.common.service.uaa.server.core.secure.URLFilterInvocationSecurityMetadataSource;
import com.orrin.sca.common.service.uaa.server.dao.SysUsersRolesRepository;
import com.orrin.sca.component.utils.string.LocalStringUtils;
import com.orrin.sca.framework.core.exception.BusinessException;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
@Service("sysUsersRolesService")
public class SysUsersRolesServiceImpl implements SysUsersRolesService {

	@Autowired
	private SysUsersRolesRepository sysUsersRolesRepository;

	@Autowired
	private SysUsersService sysUsersService;

	@Autowired
	private SysRolesService rolesService;

	@Autowired
	private URLFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource;

	@Override
	public Page<SysUsersRolesEntity> findNoCriteria(Integer page, Integer size) {
		Pageable pageable = new PageRequest(page, size);
		return sysUsersRolesRepository.findAll(pageable);
	}

	/*@Override
	public Page<SysUsersRoles> findCriteria(Integer page, Integer size, SysUsersRoles sysUsersRoles) {
		Pageable pageable = new PageRequest(page, size);
		Page<SysUsersRoles> resultPage = sysUsersRolesRepository.findAll(new Specification<SysUsersRoles>() {
			@Override
			public Predicate toPredicate(Root<SysUsersRoles> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (!StringUtils.isEmpty(sysUsersRoles.getUserId())) {
					list.add(criteriaBuilder.equal(root.get("userId").as(String.class), sysUsersRoles.getUserId()));
				}
				if (!StringUtils.isEmpty(sysUsersRoles.getId())) {
					list.add(criteriaBuilder.equal(root.get("id").as(String.class), sysUsersRoles.getId()));
				}
				if (!StringUtils.isEmpty(sysUsersRoles.getRoleId())) {
					list.add(criteriaBuilder.equal(root.get("roleId").as(String.class), sysUsersRoles.getRoleId()));
				}

				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		}, pageable);

		return resultPage;
	}*/

	@Override
	public List<SysUsersRolesEntity> findDistinctByUserId(String userId) {
		return sysUsersRolesRepository.findDistinctByUserId(userId);
	}

	@Override
	public ResponseResult<Page<SysRolesEntity>> findRolesUnderUser(String userId, RoleRequestParams roleRequestParams) {
		ResponseResult<Page<SysRolesEntity>> responseResult = new ResponseResult<>();
		responseResult.setResponseCode("00000");
		Pageable pageable = new PageRequest(roleRequestParams.getQueryPage(), roleRequestParams.getSize());
		responseResult.setData(sysUsersRolesRepository.findRolesUnderUser(userId, roleRequestParams.getRoleName(), roleRequestParams.getRoleDesc(),pageable));
		return responseResult;
	}

	@Override
	public ResponseResult<Page<SysRolesEntity>> findRolesNotUnderUser(String userId, RoleRequestParams roleRequestParams) {
		ResponseResult<Page<SysRolesEntity>> responseResult = new ResponseResult<>();
		responseResult.setResponseCode("00000");
		Pageable pageable = new PageRequest(roleRequestParams.getQueryPage(), roleRequestParams.getSize());
		responseResult.setData(sysUsersRolesRepository.findRolesNotUnderUser(userId, roleRequestParams.getRoleName(), roleRequestParams.getRoleDesc(),pageable));
		return responseResult;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult<Void> deleteRoleUnderUser(String userId, String roleId) {
		ResponseResult<Void> responseResult = new ResponseResult<>();
		responseResult.setResponseCode("00000");
		sysUsersRolesRepository.deleteByUserIdAndRoleId(userId, roleId);
		urlFilterInvocationSecurityMetadataSource.refreshResuorceMap();
		return responseResult;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResponseResult<Void> addRolesUnderUser(String userId, List<SysRolesEntity> roles) {
		ResponseResult<Void> responseResult = new ResponseResult<>();
		responseResult.setResponseCode("00000");

		SysUsersEntity userCheck = sysUsersService.findByUserId(userId);
		if(userCheck == null){
			throw new BusinessException("10000", "can not find user by userId = " + userId);
		}
		if(roles != null && roles.size() > 0 ) {
			SysUsersRolesEntity sure = new SysUsersRolesEntity();
			sure.setUserId(userId);
			for(SysRolesEntity sre : roles) {
				SysRolesEntity roleCheck = rolesService.findOne(sre.getRoleId());
				if(roleCheck == null) {
					throw new BusinessException("10000", "can not find role by roleId = " + sre.getRoleId());
				}

				SysUsersRolesEntity sureCheck = sysUsersRolesRepository.findByUserIdAndRoleId(userId, roleCheck.getRoleId());
				if(sureCheck != null) {
					throw new BusinessException("10000", "role (roleId = " + sre.getRoleId() + " ) under user ( userId = " + userId + " ) exists !");
				}

				sure.setId(LocalStringUtils.uuidLowerCase());
				sure.setRoleId(roleCheck.getRoleId());
				sysUsersRolesRepository.save(sure);
			}
		}
		urlFilterInvocationSecurityMetadataSource.refreshResuorceMap();
		return responseResult;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByUserId(String userId) {
		sysUsersRolesRepository.deleteByUserId(userId);
	}

}

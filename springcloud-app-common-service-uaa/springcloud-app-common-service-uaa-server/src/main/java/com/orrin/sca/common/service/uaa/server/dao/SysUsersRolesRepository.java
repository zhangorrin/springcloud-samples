package com.orrin.sca.common.service.uaa.server.dao;

import com.orrin.sca.common.service.uaa.client.domain.SysRolesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysUsersRolesEntity;
import com.orrin.sca.component.jpa.dao.BaseJPARepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
@Repository("sysUsersRolesRepository")
public interface SysUsersRolesRepository extends BaseJPARepository<SysUsersRolesEntity, String> {
	List<SysUsersRolesEntity> findDistinctByUserId(String userId);


	@Query(value = " select distinct sre from SysRolesEntity sre, SysUsersRolesEntity sure " +
			" where sre.roleId = sure.roleId " +
			"   and sre.enable = true " +
			"   and sure.userId = :userId " +
			"   and sre.roleName like CONCAT('%', :roleName ,'%') " +
			"   and sre.roleDesc like CONCAT('%', :roleDesc ,'%')  ")
	Page<SysRolesEntity> findRolesUnderUser(@Param("userId") String userId, @Param("roleName") String roleName, @Param("roleDesc") String roleDesc, Pageable pageable);


	@Query(value = " select distinct sre from SysRolesEntity sre " +
			" where sre.enable = true " +
			"   and sre.roleName like CONCAT('%', :roleName ,'%') " +
			"   and sre.roleDesc like CONCAT('%', :roleDesc ,'%') "+
			"and not exists (" +
			"    select 1 from  SysUsersRolesEntity sure " +
			"    where sre.roleId = sure.roleId " +
			"      and sure.userId = :userId " +
			"    ) "  )
	Page<SysRolesEntity> findRolesNotUnderUser(@Param("userId") String userId, @Param("roleName") String roleName, @Param("roleDesc") String roleDesc, Pageable pageable);

	SysUsersRolesEntity findByUserIdAndRoleId(String userId, String roleId);

	void deleteByUserIdAndRoleId(String userId, String roleId);

	void deleteByUserId(String userId);
}

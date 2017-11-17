package com.orrin.sca.common.service.uaa.server.dao;

import com.orrin.sca.common.service.uaa.client.domain.SysResourcesEntity;
import com.orrin.sca.component.jpa.dao.BaseJPARepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
@Repository("sysResourcesRepository")
public interface SysResourcesRepository extends BaseJPARepository<SysResourcesEntity, String>{

	public int countByGlobalUniqueId(String lobalUniqueId);

	@Query(value = "SELECT DISTINCT r.* FROM sys_authorities_resources ar, sys_roles_authorities ra, sys_users_roles ur, sys_resources r " +
			"WHERE ur.role_id = ra.role_id AND ra.authority_id = ar.authority_id AND ar.resource_id = r.resource_id AND ur.user_id = :userId", nativeQuery = true)
	List<SysResourcesEntity> findAuthResourcesByUserId(@Param("userId") String userId);

	@Query(value = " SELECT sr.resource_path as 'resourcePath' ," +
			" IFNULL((SELECT group_concat(DISTINCT sa.authority_mark)" +
			"    		FROM sys_authorities_resources sar," +
			"	      		 sys_authorities sa" +
			"    	   WHERE sar.resource_id = sr.resource_id" +
			"      		 AND sar.authority_id = sa.authority_id ),'AUTH_AABBCCBUGYYY') as 'authorityMark' ," +
			"      			sr.priority as 'priority' ," +
			"				sr.global_unique_id as 'globalUniqueId' , " +
			"				sr.request_method as 'requestMethod' " +
			"    	   FROM sys_resources sr" +
			"   	  WHERE sr.resource_type = 'URL'" +
			"     		AND sr.enable = 1" +
			" 	   ORDER BY sr.priority DESC ", nativeQuery = true)
	List<Object[]> findAuthResources();

	List<SysResourcesEntity> findByResourceTypeAndEnableOrderByPriorityAscZuulRouteIdAsc(String resourceType, boolean enable);
}

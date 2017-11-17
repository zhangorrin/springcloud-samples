package com.orrin.sca.common.service.uaa.server.service.impl;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesResourcesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysResourcesEntity;
import com.orrin.sca.common.service.uaa.client.service.SysAuthoritiesResourcesService;
import com.orrin.sca.common.service.uaa.client.vo.AuthoritiesAndResources;
import com.orrin.sca.common.service.uaa.server.core.secure.URLFilterInvocationSecurityMetadataSource;
import com.orrin.sca.common.service.uaa.server.dao.SysAuthoritiesRepository;
import com.orrin.sca.common.service.uaa.server.dao.SysAuthoritiesResourcesRepository;
import com.orrin.sca.common.service.uaa.server.dao.SysResourcesRepository;
import com.orrin.sca.component.utils.string.LocalStringUtils;
import com.orrin.sca.framework.core.exception.BusinessException;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
@Service("sysAuthoritiesResourcesService")
public class SysAuthoritiesResourcesServiceImpl implements SysAuthoritiesResourcesService {

	@Autowired
	private SysAuthoritiesResourcesRepository sysAuthoritiesResourcesRepository;

	@Autowired
	private SysAuthoritiesRepository sysAuthoritiesRepository;

	@Autowired
	private SysResourcesRepository sysResourcesRepository;


	@Override
	public Page<SysAuthoritiesResourcesEntity> findNoCriteria(Integer page, Integer size) {
		Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
		return sysAuthoritiesResourcesRepository.findAll(pageable);
	}

	@Override
	public ResponseResult<AuthoritiesAndResources> findAuthoritiesAndResources(String authorityId, String resourceName, Integer page, Integer size) {
		ResponseResult<AuthoritiesAndResources> responseResult = new ResponseResult<>();
		responseResult.setResponseCode("00000");
		responseResult.setResponseMsg("");

		AuthoritiesAndResources authoritiesAndResources = new AuthoritiesAndResources();
		SysAuthoritiesEntity authority = sysAuthoritiesRepository.findOne(authorityId);

		if(authority == null){
			responseResult.setResponseCode("10000");
			responseResult.setResponseMsg("could not find authority entity by authorityId");
			return responseResult;
		}

		authoritiesAndResources.setAuthority(authority);
		Page<SysResourcesEntity> resources = this.findResourcesUnderAuthority(authorityId,resourceName, page, size);
		authoritiesAndResources.setResources(resources);

		responseResult.setData(authoritiesAndResources);

		return responseResult;
	}

	private List<SysResourcesEntity> objectArray2List(List<Object[]> resourcesObjectArrayList) {
		List<SysResourcesEntity> allSreList = new ArrayList<>();

		if(resourcesObjectArrayList != null) {
			for(Object[] obj : resourcesObjectArrayList){
				String resourceId = obj[0] == null?null:obj[0].toString();
				String resourceType = obj[1] == null?null:obj[1].toString();
				String resourceNameT = obj[2] == null?null:obj[2].toString();
				String resourceDesc = obj[3] == null?null:obj[3].toString();
				String resourcePath = obj[4] == null?null:obj[4].toString();
				Integer priority = obj[5] == null ? null : (Integer) obj[5];
				Boolean enable = obj[6] == null?null:(Boolean)obj[6];
				Boolean issys = obj[7] == null?null:(Boolean)obj[7];
				String moduleId = obj[8] == null?null:obj[8].toString();
				String globalUniqueId = obj[9] == null?null:obj[9].toString();
				String fatherResourceId = obj[10] == null?null:obj[10].toString();
				String icon = obj[11] == null?null:obj[11].toString();
				String requestMethod = obj[12] == null?null:obj[12].toString();
				String createdBy = obj[13] == null?null:obj[13].toString();
				Date createdDate = obj[14] == null?null:(Date)obj[14];
				String lastModifiedBy = obj[15] == null?null:obj[15].toString();
				Date lastModifiedDate = obj[16] == null?null:(Date)obj[16];
				String clientId = obj[17] == null?null:obj[17].toString();
				SysResourcesEntity sre = new SysResourcesEntity(resourceId, resourceType, resourceNameT,
						resourceDesc, resourcePath, priority, enable, issys, moduleId, globalUniqueId,
						fatherResourceId, icon, requestMethod, createdBy, createdDate, lastModifiedBy, lastModifiedDate,clientId);

				allSreList.add(sre);
			}
		}
		return allSreList;
	}

	@Override
	public Page<SysResourcesEntity> findResourcesUnderAuthority(String authorityId, String resourceName, Integer page, Integer size) {
		List<Object[]> sreList = sysAuthoritiesResourcesRepository.findResourcesByAuthorityId(authorityId,resourceName, page*size, size);

		Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "resourceId");

		List<Object> countOjb = sysAuthoritiesResourcesRepository.countResourcesByAuthorityId(authorityId,resourceName);
		BigInteger count = (countOjb.get(0) instanceof BigInteger) ? (BigInteger)countOjb.get(0) : BigInteger.valueOf(0);

		List<SysResourcesEntity> allSreList = objectArray2List(sreList);

		Page<SysResourcesEntity> srePage = new PageImpl<SysResourcesEntity>(allSreList,pageable,count.longValue());
		return srePage;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByAuthorityId(String authorityId) {
		sysAuthoritiesResourcesRepository.deleteByAuthorityId(authorityId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public long countByAuthorityId(String authorityId) {
		return sysAuthoritiesResourcesRepository.countByAuthorityId(authorityId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByAuthorityIdAndResourceId(String authorityId, String resourceId) {
		sysAuthoritiesResourcesRepository.deleteByAuthorityIdAndResourceId(authorityId, resourceId);
	}

	@Override
	public Page<SysResourcesEntity> findResourcesNotUnderAuthoritiy(String authorityId, String resourceName, Integer page, Integer size) {
		List<Object[]> sreList = sysAuthoritiesResourcesRepository.findResourcesNotUnderAuthority(authorityId,resourceName, page*size, size);

		Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "resourceId");

		List<Object> countOjb = sysAuthoritiesResourcesRepository.countResourcesNotUnderAuthority(authorityId,resourceName);
		BigInteger count = (countOjb.get(0) instanceof BigInteger) ? (BigInteger)countOjb.get(0) : BigInteger.valueOf(0);

		List<SysResourcesEntity> allSreList = objectArray2List(sreList);

		Page<SysResourcesEntity> srePage = new PageImpl<SysResourcesEntity>(allSreList,pageable,count.longValue());
		return srePage;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void addResourcesUnderAuthoritiy(String authorityId, List<SysResourcesEntity> resources) {
		SysAuthoritiesEntity sysAuthoritiesEntityCheck = sysAuthoritiesRepository.findOne(authorityId);

		if(sysAuthoritiesEntityCheck == null) {
			throw new BusinessException("10000", " authority id : " + authorityId + " not exists !");
		}

		if(resources != null && resources.size() > 0){
			SysAuthoritiesResourcesEntity sysAuthoritiesResourcesEntity = new SysAuthoritiesResourcesEntity();
			sysAuthoritiesResourcesEntity.setAuthorityId(authorityId);
			for(SysResourcesEntity sre : resources) {

				SysResourcesEntity sreCheck = sysResourcesRepository.findOne(sre.getResourceId());
				if(sreCheck == null) {
					throw new BusinessException("10000", " resource id : " + sre.getResourceId() + " not exists !");
				}

				sysAuthoritiesResourcesEntity.setResourceId(sreCheck.getResourceId());

				SysAuthoritiesResourcesEntity sareCheck = sysAuthoritiesResourcesRepository.findByAuthorityIdAndResourceId(authorityId, sreCheck.getResourceId());
				if(sareCheck != null) {
					throw new BusinessException("10000", " resource id : " + sre.getResourceId() + " under : " + authorityId + " exists !");
				}

				sysAuthoritiesResourcesEntity.setId(LocalStringUtils.uuidLowerCase());

				sysAuthoritiesResourcesRepository.save(sysAuthoritiesResourcesEntity);
			}
		}
	}


}

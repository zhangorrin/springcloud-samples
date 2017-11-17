package com.orrin.sca.common.service.uaa.client.service;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import org.springframework.data.domain.Page;

/**
 * @author Orrin on 2017/7/8.
 */
public interface SysAuthoritiesService {

	Page<SysAuthoritiesEntity> findNoCriteria(Integer page, Integer size);
	//Page<SysAuthoritiesEntity> findCriteria(Integer page, Integer size, SysAuthoritiesEntity sysAuthorities);

	void deleteAuthority(String authorityId);

	SysAuthoritiesEntity findOne(String authorityId);
}

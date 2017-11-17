package com.orrin.sca.common.service.uaa.server.service.impl;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.service.SysAuthoritiesResourcesService;
import com.orrin.sca.common.service.uaa.client.service.SysAuthoritiesService;
import com.orrin.sca.common.service.uaa.server.dao.SysAuthoritiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Orrin on 2017/7/8.
 */
@Service("sysAuthoritiesService")
public class SysAuthoritiesServiceImpl implements SysAuthoritiesService {

	@Autowired
	private SysAuthoritiesRepository sysAuthoritiesRepository;

	@Autowired
	private SysAuthoritiesResourcesService sysAuthoritiesResourcesService;

	@Override
	public Page<SysAuthoritiesEntity> findNoCriteria(Integer page, Integer size) {
		Pageable pageable = new PageRequest(page, size);
		return sysAuthoritiesRepository.findAll(pageable);
	}

	/*@Override
	public Page<SysAuthorities> findCriteria(Integer page, Integer size, SysAuthorities sysAuthorities) {
		return null;
	}*/

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAuthority(String authorityId) {
		sysAuthoritiesRepository.delete(authorityId);
		sysAuthoritiesResourcesService.deleteByAuthorityId(authorityId);
	}

	@Override
	public SysAuthoritiesEntity findOne(String authorityId) {
		return sysAuthoritiesRepository.findOne(authorityId);
	}
}

package com.orrin.sca.common.service.uaa.server.service.impl;

import com.orrin.sca.common.service.uaa.client.domain.SysModulesEntity;
import com.orrin.sca.common.service.uaa.client.service.SysModulesService;
import com.orrin.sca.common.service.uaa.server.dao.SysModulesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Orrin on 2017/7/8.
 */
@Service("sysModulesService")
public class SysModulesServiceImpl implements SysModulesService {

	@Autowired
	private SysModulesRepository sysModulesRepository;

	@Override
	public Page<SysModulesEntity> findNoCriteria(Integer page, Integer size) {
		Pageable pageable = new PageRequest(page, size);
		return sysModulesRepository.findAll(pageable);
	}

	/*@Override
	public Page<SysModules> findCriteria(Integer page, Integer size, SysModules sysModules) {
		return null;
	}*/
}

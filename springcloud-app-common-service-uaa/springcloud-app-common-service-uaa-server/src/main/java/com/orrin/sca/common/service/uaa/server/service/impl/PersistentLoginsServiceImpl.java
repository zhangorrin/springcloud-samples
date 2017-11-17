package com.orrin.sca.common.service.uaa.server.service.impl;

import com.orrin.sca.common.service.uaa.client.domain.PersistentLoginsEntity;
import com.orrin.sca.common.service.uaa.client.service.PersistentLoginsService;
import com.orrin.sca.common.service.uaa.server.dao.PersistentLoginsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * @author Orrin on 2017/7/8.
 */
@Service("persistentLoginsService")
public class PersistentLoginsServiceImpl implements PersistentLoginsService {

	@Autowired
	private PersistentLoginsRepository persistentLoginsRepository;

	@Override
	public Page<PersistentLoginsEntity> findNoCriteria(Integer page, Integer size) {
		Pageable pageable = new PageRequest(page, size, Sort.Direction.DESC, "last_used");
		return persistentLoginsRepository.findAll(pageable);
	}

}

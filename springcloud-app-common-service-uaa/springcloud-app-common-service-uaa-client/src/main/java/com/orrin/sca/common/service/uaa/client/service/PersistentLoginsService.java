package com.orrin.sca.common.service.uaa.client.service;


import com.orrin.sca.common.service.uaa.client.domain.PersistentLoginsEntity;
import org.springframework.data.domain.Page;

/**
 * @author Orrin on 2017/7/8.
 */
public interface PersistentLoginsService {
	Page<PersistentLoginsEntity> findNoCriteria(Integer page, Integer size);
}

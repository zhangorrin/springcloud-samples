package com.orrin.sca.common.service.uaa.client.service;

import com.orrin.sca.common.service.uaa.client.domain.SysUsersEntity;
import com.orrin.sca.component.jpa.dao.Range;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
public interface SysUsersService {
	Page<SysUsersEntity> findSysUsersNoCriteria(Integer page, Integer size);
	//Page<SysUsersEntity> findSysUsersCriteria(Integer page, Integer size, SysUsersEntity sysUsers);

	Page<SysUsersEntity> queryByExampleWithRange(Example example, List<Range<SysUsersEntity>> ranges, Pageable pageable);

	SysUsersEntity findByUsername(String username);

	SysUsersEntity findByMobile(String mobile);

	SysUsersEntity findByUserId(String userId);

	SysUsersEntity saveAndFlush(SysUsersEntity sysUsersEntity);

	void deleteByUserId(String userId);
}

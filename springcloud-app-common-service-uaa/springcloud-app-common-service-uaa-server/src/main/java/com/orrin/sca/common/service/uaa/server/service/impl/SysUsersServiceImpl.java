package com.orrin.sca.common.service.uaa.server.service.impl;

import com.orrin.sca.common.service.uaa.client.domain.SysUsersEntity;
import com.orrin.sca.common.service.uaa.client.service.SysUsersRolesService;
import com.orrin.sca.common.service.uaa.client.service.SysUsersService;
import com.orrin.sca.common.service.uaa.server.dao.SysUsersRepository;
import com.orrin.sca.component.jpa.dao.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Orrin on 2017/7/8.
 */
@Service("sysUsersService")
public class SysUsersServiceImpl implements SysUsersService {

	@Autowired
	private SysUsersRepository sysUsersRepository;

	@Autowired
	private SysUsersRolesService sysUsersRolesService;

	@Override
	public Page<SysUsersEntity> findSysUsersNoCriteria(Integer page, Integer size) {
		Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "userId");
		List<SysUsersEntity> listAll = sysUsersRepository.findAll();
		Page<SysUsersEntity> sysUsersPage = sysUsersRepository.findAll(pageable);
		return sysUsersPage;
	}

	@Override
	public Page<SysUsersEntity> queryByExampleWithRange(Example example, List<Range<SysUsersEntity>> ranges, Pageable pageable) {
		return sysUsersRepository.queryByExampleWithRange(example, ranges, pageable);
	}

	/*@Override
	public Page<SysUsers> findSysUsersCriteria(Integer page, Integer size, SysUsers sysUsers) {

		Pageable pageable = new PageRequest(page, size);
		Page<SysUsers> resultPage = sysUsersRepository.findAll(new Specification<SysUsers>() {
			@Override
			public Predicate toPredicate(Root<SysUsers> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (!StringUtils.isEmpty(sysUsers.getUserId())) {
					list.add(criteriaBuilder.equal(root.get("user_id").as(String.class), sysUsers.getUserId()));
				}
				if (!StringUtils.isEmpty(sysUsers.getUsername())) {
					list.add(criteriaBuilder.equal(root.get("username").as(String.class), sysUsers.getUsername()));
				}
				if (!StringUtils.isEmpty(sysUsers.getName())) {
					list.add(criteriaBuilder.equal(root.get("name").as(String.class), sysUsers.getName()));
				}
				if (!StringUtils.isEmpty(sysUsers.getEnabled())) {
					list.add(criteriaBuilder.equal(root.get("enabled").as(Long.class), sysUsers.getEnabled()));
				}
				if (!StringUtils.isEmpty(sysUsers.getAccountNonExpired())) {
					list.add(criteriaBuilder.equal(root.get("account_non_expired").as(Long.class), sysUsers.getAccountNonExpired()));
				}
				if (!StringUtils.isEmpty(sysUsers.getAccountNonLocked())) {
					list.add(criteriaBuilder.equal(root.get("account_non_locked").as(Long.class), sysUsers.getAccountNonLocked()));
				}
				if (!StringUtils.isEmpty(sysUsers.getCredentialsNonExpired())) {
					list.add(criteriaBuilder.equal(root.get("credentials_non_expired").as(Long.class), sysUsers.getCredentialsNonExpired()));
				}


				Predicate[] p = new Predicate[list.size()];
				return criteriaBuilder.and(list.toArray(p));
			}
		}, pageable);

		return resultPage;
	}*/

	@Override
	public SysUsersEntity findByUsername(String username) {
		return sysUsersRepository.findByUsername(username);
	}

	@Override
	public SysUsersEntity findByMobile(String mobile) {
		return sysUsersRepository.findByMobile(mobile);
	}

	@Override
	public SysUsersEntity findByUserId(String userId) {
		return sysUsersRepository.findOne(userId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public SysUsersEntity saveAndFlush(SysUsersEntity sysUsersEntity) {
		if(sysUsersEntity!=null && !StringUtils.hasText(sysUsersEntity.getPassword())){
			sysUsersEntity.setPassword("123456");
		}
		return sysUsersRepository.saveAndFlush(sysUsersEntity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByUserId(String userId) {
		sysUsersRepository.delete(userId);
		sysUsersRolesService.deleteByUserId(userId);
	}
}

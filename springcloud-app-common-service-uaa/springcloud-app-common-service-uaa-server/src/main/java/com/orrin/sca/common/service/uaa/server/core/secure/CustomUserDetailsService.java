package com.orrin.sca.common.service.uaa.server.core.secure;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysUsersEntity;
import com.orrin.sca.common.service.uaa.client.service.SysUsersService;
import com.orrin.sca.common.service.uaa.server.dao.SysAuthoritiesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Orrin on 2017/7/10.
 */
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private SysUsersService sysUsersService;

	@Autowired
	private SysAuthoritiesRepository sysAuthoritiesRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		SysUsersEntity sysUser = sysUsersService.findByUsername(username);
		if (sysUser == null) {
			throw new UsernameNotFoundException("用户名不存在");
		}

		logger.info("username:" + username);

		List<SysAuthoritiesEntity> sysAuthorities = sysAuthoritiesRepository.findAuthorityByUserId(sysUser.getUserId());

		CurrentSessionUser currentSessionUser = CurrentSessionUser.createCurrentSessionUser(sysUser, sysAuthorities);

		if(!currentSessionUser.isAccountNonLocked()){
			throw new LockedException("账号被锁定");
		}

		if(!currentSessionUser.isEnabled()){
			throw new DisabledException("账号不可用");
		}

		if(!currentSessionUser.isCredentialsNonExpired()){
			throw new BadCredentialsException("证书过期");
		}

		if(!currentSessionUser.isAccountNonExpired()){
			throw new AccountExpiredException("账户过期");
		}

		return currentSessionUser;
	}
}

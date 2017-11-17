package com.orrin.sca.common.service.uaa.server.core.secure;

import com.orrin.sca.common.service.uaa.client.domain.SysAuthoritiesEntity;
import com.orrin.sca.common.service.uaa.client.domain.SysUsersEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * @author Orrin on 2017/7/10.
 */
public class CurrentSessionUser implements UserDetails {


	private String userId;

	private String username;

	private String name;

	private String password;

	private String depId;

	private String depName;

	private Boolean enabled;

	private Boolean accountNonExpired;

	private Boolean accountNonLocked;

	private Boolean credentialsNonExpired;

	private List<SysAuthoritiesEntity> sysAuthorities;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<>();
		List<SysAuthoritiesEntity> authoritiesTemp = this.getSysAuthorities();
		Set<String> nonRepetitionSet = new HashSet<>();
		for (SysAuthoritiesEntity sas : authoritiesTemp) {
			if(!nonRepetitionSet.contains(sas.getAuthorityMark())){
				nonRepetitionSet.add(sas.getAuthorityMark());
				auths.add(new SimpleGrantedAuthority(sas.getAuthorityMark()));
			}
		}
		return auths;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepId() {
		return depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(Boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public Boolean getCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public List<SysAuthoritiesEntity> getSysAuthorities() {
		return sysAuthorities;
	}

	public void setSysAuthorities(List<SysAuthoritiesEntity> sysAuthorities) {
		this.sysAuthorities = sysAuthorities;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public static CurrentSessionUser createCurrentSessionUser(SysUsersEntity sysUsers, List<SysAuthoritiesEntity> authorities){
		CurrentSessionUser sessionUser = new CurrentSessionUser();
		BeanUtils.copyProperties(sysUsers,sessionUser);
		sessionUser.setSysAuthorities(authorities);
		return sessionUser;
	}
}

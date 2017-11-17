package com.orrin.sca.common.service.uaa.server.core.secure;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Orrin on 2017/7/12.
 * 自定义AbstractAccessDecisionManager权限决策类
 */
public class MyAccessDecisionManager extends AbstractAccessDecisionManager {
	public MyAccessDecisionManager(List<AccessDecisionVoter<? extends Object>> decisionVoters) {
		super(decisionVoters);
	}

	@Override
	public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
		if(configAttributes==null){
			return;
		}
		Iterator<ConfigAttribute> ite = configAttributes.iterator();
		while(ite.hasNext()){
			ConfigAttribute ca = ite.next();
			String needRole = (ca).getAttribute();
			if("permitAll".equalsIgnoreCase(needRole)){
				return;
			}
			for (GrantedAuthority ga : authentication.getAuthorities()){
				if (ga.getAuthority().equals(needRole)){
					return;
				}
			}
		}
		throw new AccessDeniedException("没有权限,拒绝访问!");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return false;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}

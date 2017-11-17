package com.orrin.sca.common.service.uaa.server.core.secure;

import com.orrin.sca.component.privilege.intercept.URLFilterInvocationAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;

/**
 * @author Orrin on 2017/7/12.
 */
public class URLFilterInvocationSecurityMetadataSource  implements FilterInvocationSecurityMetadataSource,InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(URLFilterInvocationSecurityMetadataSource.class);

	@Autowired
	private URLFilterInvocationAuthority urlFilterInvocationAuthority;

	@Override
	public void afterPropertiesSet() throws Exception {
		//urlFilterInvocationAuthority.refreshResuorceAttributes();
		logger.info("资源权限列表 init finished");
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		long startTime = System.currentTimeMillis();

		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		String requestRoot = "";

		Collection<ConfigAttribute> attrs = urlFilterInvocationAuthority.getAttributes(requestRoot, request);

		logger.info("URL资源match："+request.getRequestURI()+ " -> " + attrs);
		logger.info("URL资源match execute TimeMillis = {} ", (System.currentTimeMillis() - startTime));
		return attrs;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = urlFilterInvocationAuthority.getAllConfigAttributes();
		return allAttributes;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return FilterInvocation.class.isAssignableFrom(aClass);
	}

	public void refreshResuorceMap(){
		this.urlFilterInvocationAuthority.refreshResuorceAttributes();
	}

}

package com.orrin.sca.component.jpa.parent;

import com.orrin.sca.component.utils.security.SecurityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

/**
 * @author orrin.zhang on 2017/8/3.
 * 需要审计功能，加上@EnableJpaAuditing(auditorAwareRef = "auditorAware")
 */
public class ParentApplication {

	@Bean(name = "auditorAware")
	public AuditorAware<String> auditorAware() {
		return ()-> SecurityUtils.getCurrentUserUsername();
	}
}

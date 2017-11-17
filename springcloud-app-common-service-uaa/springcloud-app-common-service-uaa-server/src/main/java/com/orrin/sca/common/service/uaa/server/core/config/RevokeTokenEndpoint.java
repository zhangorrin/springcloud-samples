package com.orrin.sca.common.service.uaa.server.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author orrin.zhang on 2017/8/2.
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {

	@Autowired
	@Qualifier("consumerTokenServices")
	ConsumerTokenServices consumerTokenServices;

	@RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
	@ResponseBody
	public String revokeToken(String access_token) {
		if (consumerTokenServices.revokeToken(access_token)){
			return "注销成功";
		}else{
			return "注销失败";
		}
	}
}
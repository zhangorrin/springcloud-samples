package com.orrin.sca.common.service.apigateway.server.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.orrin.sca.common.service.uaa.client.feignclient.SysResourceServiceApi;
import com.orrin.sca.component.jwt.CoverAccessTokenModel;
import com.orrin.sca.component.privilege.intercept.URLFilterInvocationAuthority;
import com.orrin.sca.component.utils.json.JacksonUtils;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.discovery.DiscoveryClientRouteLocator;
import org.springframework.http.MediaType;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author orrin.zhang on 2017/7/28.
 */
public class PreFilter extends ZuulFilter {

	private static Logger log = LoggerFactory.getLogger(PreFilter.class);

	/**
	 * pre：可以在请求被路由之前调用
	 * route：在路由请求时候被调用
	 * error：处理请求时发生错误时被调用
	 * post：在route和error过滤器之后被调用
	 * @return
	 */
	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Autowired
	private DiscoveryClient discovery;

	@Autowired
	private DiscoveryClientRouteLocator routeLocator;

	@Autowired
	private URLFilterInvocationAuthority urlFilterInvocationAuthority;

	@Autowired
	private SysResourceServiceApi sysResourceServiceApi;

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();

		Object bestMatchingPattern = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

		log.info("[*]bestMatchingPattern = {}", bestMatchingPattern == null ? "null" : bestMatchingPattern.toString());

		log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
		String accessToken = request.getHeader("Authorization");

		log.info(" accessToken = {}",accessToken);
		String AuthorizationPrefix = accessToken.substring(0, 7).toLowerCase();
		if(AuthorizationPrefix.equalsIgnoreCase("Bearer ")) {
			accessToken = accessToken.substring(7);
		}

		ResponseResult<?> responseResult = null;

		if (!StringUtils.hasText(accessToken)) {
			log.warn("token is empty");
			responseResult = determineAccessAuthority(null);
		}else {
			Jwt jwt = JwtHelper.decode(accessToken);
			responseResult = determineAccessAuthority(jwt);
		}
		log.info("ok");
		if(responseResult != null && !responseResult.getResponseCode().equalsIgnoreCase("00000")) {
			ctx.setSendZuulResponse(false);
			ctx.getResponse().setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			ctx.setResponseStatusCode(401);
			ctx.setResponseBody(JacksonUtils.encode(responseResult));
			ctx.setResponseGZipped(true);
		}
		return null;
	}

	protected ResponseResult<?> determineAccessAuthority(Jwt jwt) {
		ResponseResult<?> responseResult = new ResponseResult<>();
		responseResult.setResponseCode("00000");

		CoverAccessTokenModel coverAccessTokenModel = null;
		if(jwt != null) {
			coverAccessTokenModel = JacksonUtils.decode(jwt.getClaims(), CoverAccessTokenModel.class);

			long nowTime = System.currentTimeMillis() / 1000;

			if(nowTime > coverAccessTokenModel.getExp()){
				responseResult.setResponseCode("10001");
				responseResult.setResponseMsg("token is expired !");
				return responseResult;
			}
		}

		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		Route route = routeLocator.getMatchingRoute(request.getRequestURI());

		Collection<ConfigAttribute> attributes = urlFilterInvocationAuthority.getAttributes(route.getPrefix(), request);

		if(attributes != null && attributes.size() > 0){

			if(coverAccessTokenModel.getAuthorities() == null || coverAccessTokenModel.getAuthorities().size() == 0) {
				responseResult.setResponseCode("10001");
				responseResult.setResponseMsg("您可能没有权限使用网络资源 ("+ route.getFullPath() +") !");
				return responseResult;
			}

			boolean hasAttribute = false;
			for(ConfigAttribute ca : attributes) {
				if(coverAccessTokenModel.getAuthorities().contains(ca.getAttribute())) {
					hasAttribute = true;
					break;
				}
			}
			if(!hasAttribute) {
				responseResult.setResponseCode("10001");
				responseResult.setResponseMsg("您可能没有权限使用网络资源 ("+ route.getFullPath() +") !");
				return responseResult;
			}
		}

		return responseResult;
	}
}
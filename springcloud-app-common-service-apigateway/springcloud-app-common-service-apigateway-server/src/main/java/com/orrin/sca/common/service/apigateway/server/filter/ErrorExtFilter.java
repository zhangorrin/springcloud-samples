package com.orrin.sca.common.service.apigateway.server.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * @author orrin.zhang on 2017/7/28.
 */
@Component
public class ErrorExtFilter extends SendErrorFilter {

	private static Logger log = LoggerFactory.getLogger(ErrorExtFilter.class);

	@Override
	public String filterType() {
		return "error";
	}

	@Override
	public int filterOrder() {
		return 30;
	}

	@Override
	public boolean shouldFilter() {
		//判断：仅处理来自post过滤器引起的异常（CustomeFilterProcessor）

		RequestContext ctx = RequestContext.getCurrentContext();
		ZuulFilter failedFilter = (ZuulFilter) ctx.get("failed.filter");

		if(failedFilter != null && failedFilter.filterType().equals("post")) {
			return true;
		}

		return false;
	}

}
package com.orrin.sca.common.service.apigateway.server.config;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.web.context.request.RequestAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author migu-orrin on 2018/2/11.
 * 自定义错误信息
 */
public class CustomerErrorAttributes extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
		Map<String, Object> newResult = new HashMap<>();
		Map<String, Object> result = super.getErrorAttributes(requestAttributes, includeStackTrace);
		newResult.put("responseCode", result.get("status"));
		newResult.put("responseMsg", result.get("message"));
		newResult.put("data", result);
		return newResult;
	}
}

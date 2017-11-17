package com.orrin.sca.common.service.uaa.server.core.secure;

import com.orrin.sca.component.utils.json.JacksonUtils;
import com.orrin.sca.framework.core.model.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Orrin on 2017/7/12.
 */
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

	private static final Logger logger = LoggerFactory.getLogger(DefaultAccessDeniedHandler.class);
	/* (non-Javadoc)
	 * @see org.springframework.security.web.access.AccessDeniedHandler#handle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.access.AccessDeniedException)
	 */
	private String errorPage;

	private static final String AJAX_ACCEPT_CONTENT_TYPE = "text/html;type=ajax";
	private static final String AJAX_SOURCE_PARAM = "ajaxSource";
	public boolean isAjaxRequestInternal(HttpServletRequest request) {
		String acceptHeader = request.getHeader("Accept");
		String ajaxParam = request.getHeader(AJAX_SOURCE_PARAM);
		if (AJAX_ACCEPT_CONTENT_TYPE.equals(acceptHeader) || StringUtils.hasText(ajaxParam)) {
			return true;
		} else {
			return false;
		}
	}

	//~ Methods ========================================================================================================

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		boolean isAjax = isAjaxRequestInternal(request);
		if(isAjax){
			//处理ajax请求错误
			logger.info(" access denied is ajax = {}", isAjax);
			ResponseResult<Void> responseResult = new ResponseResult<>();
			responseResult.setResponseCode("20000");
			responseResult.setResponseMsg("not permission !");

			request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

			PrintWriter printWriter = response.getWriter();
			printWriter.append(JacksonUtils.encode(responseResult));
			printWriter.close();

		}else if (!response.isCommitted()) {
			if (errorPage != null) {
				// Put exception into request scope (perhaps of use to a view)
				request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);

				// Set the 403 status code.
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);

				// forward to error page.
				RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
				dispatcher.forward(request, response);
			} else {
				response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
			}
		}
	}

	/**
	 * The error page to use. Must begin with a "/" and is interpreted relative to the current context root.
	 *
	 * @param errorPage the dispatcher path to display
	 *
	 * @throws IllegalArgumentException if the argument doesn't comply with the above limitations
	 */
	public void setErrorPage(String errorPage) {
		if ((errorPage != null) && !errorPage.startsWith("/")) {
			throw new IllegalArgumentException("errorPage must begin with '/'");
		}

		logger.info("init DefaultAccessDeniedHandler , errorPage = {}", errorPage);
		this.errorPage = errorPage;
	}

}
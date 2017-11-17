package com.orrin.sca.common.service.uaa.server.core.secure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetailsSource;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.Assert;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author migu-orrin on 2017/11/16.
 */
public class CustomeOAuth2AuthenticationProcessingFilter implements Filter, InitializingBean {

	private final static Log logger = LogFactory.getLog(OAuth2AuthenticationProcessingFilter.class);

	private AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();

	private AuthenticationManager authenticationManager;

	private AuthenticationManager authenticationManagerDelegator;

	private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new OAuth2AuthenticationDetailsSource();

	private TokenExtractor tokenExtractor = new BearerTokenExtractor();

	private AuthenticationEventPublisher eventPublisher = new NullEventPublisher();

	private String credentialsCharset = "UTF-8";

	private boolean stateless = true;

	public void setAuthenticationManagerDelegator(AuthenticationManager authenticationManagerDelegator) {
		this.authenticationManagerDelegator = authenticationManagerDelegator;
	}

	/**
	 * Flag to say that this filter guards stateless resources (default true). Set this to true if the only way the
	 * resource can be accessed is with a token. If false then an incoming cookie can populate the security context and
	 * allow access to a caller that isn't an OAuth2 client.
	 *
	 * @param stateless the flag to set (default true)
	 */
	public void setStateless(boolean stateless) {
		this.stateless = stateless;
	}

	/**
	 * @param authenticationEntryPoint the authentication entry point to set
	 */
	public void setAuthenticationEntryPoint(AuthenticationEntryPoint authenticationEntryPoint) {
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	/**
	 * @param authenticationManager the authentication manager to set (mandatory with no default)
	 */
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	/**
	 * @param tokenExtractor the tokenExtractor to set
	 */
	public void setTokenExtractor(TokenExtractor tokenExtractor) {
		this.tokenExtractor = tokenExtractor;
	}

	/**
	 * @param eventPublisher the event publisher to set
	 */
	public void setAuthenticationEventPublisher(AuthenticationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	/**
	 * @param authenticationDetailsSource The AuthenticationDetailsSource to use
	 */
	public void setAuthenticationDetailsSource(
			AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
		Assert.notNull(authenticationDetailsSource, "AuthenticationDetailsSource required");
		this.authenticationDetailsSource = authenticationDetailsSource;
	}

	public void afterPropertiesSet() {
		Assert.state(authenticationManager != null, "AuthenticationManager is required");
	}

	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {
		byte[] base64Token = header.substring(6).getBytes("UTF-8");

		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException var7) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, credentialsCharset);
		int delim = token.indexOf(":");
		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		} else {
			return new String[]{token.substring(0, delim), token.substring(delim + 1)};
		}
	}

	private boolean authenticationIsRequired(String username) {
		Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
		if (existingAuth != null && existingAuth.isAuthenticated()) {
			if (existingAuth instanceof UsernamePasswordAuthenticationToken && !existingAuth.getName().equals(username)) {
				return true;
			} else {
				return existingAuth instanceof AnonymousAuthenticationToken;
			}
		} else {
			return true;
		}
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException {

		final boolean debug = logger.isDebugEnabled();
		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;

		try {

			Authentication authentication = tokenExtractor.extract(request);

			if (authentication == null) {
				if (stateless && isAuthenticated()) {
					if (debug) {
						logger.debug("Clearing security context.");
					}
					SecurityContextHolder.clearContext();
				}
				if (debug) {
					logger.debug("No token in request, will continue chain.");
				}

				String header = request.getHeader("Authorization");
				if (header != null && header.startsWith("Basic ")) {
					String[] tokens = this.extractAndDecodeHeader(header, request);
					assert tokens.length == 2;

					String username = tokens[0];
					if (debug) {
						this.logger.debug("Basic Authentication Authorization header found for user '" + username + "'");
					}

					boolean go = this.authenticationIsRequired(username);
					if (go) {
						UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, tokens[1]);
						authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
						Authentication authResult = this.authenticationManagerDelegator.authenticate(authRequest);
						if (debug) {
							this.logger.debug("Authentication success: " + authResult);
						}
						SecurityContextHolder.getContext().setAuthentication(authResult);
					}

				}
			}
			else {
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, authentication.getPrincipal());
				if (authentication instanceof AbstractAuthenticationToken) {
					AbstractAuthenticationToken needsDetails = (AbstractAuthenticationToken) authentication;
					needsDetails.setDetails(authenticationDetailsSource.buildDetails(request));
				}
				Authentication authResult = authenticationManager.authenticate(authentication);

				if (debug) {
					logger.debug("Authentication success: " + authResult);
				}

				eventPublisher.publishAuthenticationSuccess(authResult);
				SecurityContextHolder.getContext().setAuthentication(authResult);

			}
		}
		catch (OAuth2Exception failed) {
			SecurityContextHolder.clearContext();

			if (debug) {
				logger.debug("Authentication request failed: " + failed);
			}
			eventPublisher.publishAuthenticationFailure(new BadCredentialsException(failed.getMessage(), failed),
					new PreAuthenticatedAuthenticationToken("access-token", "N/A"));

			authenticationEntryPoint.commence(request, response,
					new InsufficientAuthenticationException(failed.getMessage(), failed));

			return;
		}

		chain.doFilter(request, response);
	}

	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		return true;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}

	private static final class NullEventPublisher implements AuthenticationEventPublisher {
		public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		}

		public void publishAuthenticationSuccess(Authentication authentication) {
		}
	}
}

package com.orrin.sca.common.service.uaa.server.core.oauth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.*;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.session.ExpiringSession;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.util.Assert;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A pre-authentication filter for OAuth2 protected resources. Extracts an OAuth2 token from the incoming request and
 * uses it to populate the Spring Security context with an {@link OAuth2Authentication} (if used in conjunction with an
 * {@link OAuth2AuthenticationManager}).
 *
 * @author Dave Syer
 *
 */
public class CustomeOAuth2AuthenticationProcessingFilter implements Filter, InitializingBean {

    private final static Log logger = LogFactory.getLog(OAuth2AuthenticationProcessingFilter.class);

    private AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();

    private AuthenticationManager authenticationManager;

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new OAuth2AuthenticationDetailsSource();

    private TokenExtractor tokenExtractor = new BearerTokenExtractor();

    private AuthenticationEventPublisher eventPublisher = new NullEventPublisher();

    private boolean stateless = true;

    private RedisOperationsSessionRepository redisOperationsSessionRepository;
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


                ExpiringSession redisSession = redisOperationsSessionRepository.getSession("");

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
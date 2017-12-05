package com.orrin.sca.common.service.uaa.server.core.config;

import com.orrin.sca.common.service.uaa.server.core.oauth.ClientDetailsServiceImpl;
import com.orrin.sca.common.service.uaa.server.core.secure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author orrin.zhang on 2017/7/28.
 */
//@Order(ManagementServerProperties.ACCESS_OVERRIDE_ORDER)
//@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityConfig.class);

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private ClientDetailsServiceImpl clientDetailsService;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(5);
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		//daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		daoAuthenticationProvider.setHideUserNotFoundExceptions(false);

		return daoAuthenticationProvider;
	}

	@Bean
	DefaultAccessDeniedHandler accessDeniedHandler() {
		DefaultAccessDeniedHandler accessDeniedHandler = new DefaultAccessDeniedHandler();
		accessDeniedHandler.setErrorPage("/securityException/accessDenied");
		return accessDeniedHandler;
	}

	@Bean
	AuthenticationSuccessHandler athenticationSuccessHandler() {
		SimpleLoginSuccessHandler simpleLoginSuccessHandler = new SimpleLoginSuccessHandler();
		return simpleLoginSuccessHandler;
	}

	@Bean
	URLFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() {
		URLFilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource = new URLFilterInvocationSecurityMetadataSource();
		return urlFilterInvocationSecurityMetadataSource;
	}


	@Bean(name = "authenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() {
		AuthenticationManager authenticationManager = null;
		try {
			authenticationManager = super.authenticationManagerBean();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authenticationManager;
	}

	/*
	 *
	 * 这里可以增加自定义的投票器
	 */
	@Bean(name = "accessDecisionManager")
	public AccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList();
		decisionVoters.add(new RoleVoter());
		decisionVoters.add(new AuthenticatedVoter());
		decisionVoters.add(webExpressionVoter());// 启用表达式投票器
		MyAccessDecisionManager accessDecisionManager = new MyAccessDecisionManager(decisionVoters);
		return accessDecisionManager;
	}

	/*
	* 表达式控制器
	*/
	@Bean(name = "expressionHandler")
	public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
		DefaultWebSecurityExpressionHandler webSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		return webSecurityExpressionHandler;
	}

	/*
	 * 表达式投票器
	 */
	@Bean(name = "expressionVoter")
	public WebExpressionVoter webExpressionVoter() {
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		webExpressionVoter.setExpressionHandler(webSecurityExpressionHandler());
		return webExpressionVoter;
	}

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/", "/mgmt/health", "/securityException/accessDenied","/login","/oauth/token", "/oauth/authorize", "/oauth/confirm_access").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/oauth/token", "/**").permitAll();
		//http.formLogin().loginPage("/login").permitAll().and().authorizeRequests().anyRequest().authenticated();

		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(new RedisTokenStore(redisConnectionFactory));
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setClientDetailsService(clientDetailsService);

		OAuth2AuthenticationManager oauthAuthenticationManager = new OAuth2AuthenticationManager();
		oauthAuthenticationManager.setResourceId("");
		oauthAuthenticationManager.setTokenServices(tokenServices);
		oauthAuthenticationManager.setClientDetailsService(clientDetailsService);

		CustomeOAuth2AuthenticationProcessingFilter oapf = new CustomeOAuth2AuthenticationProcessingFilter();
		oapf.setAuthenticationManager(oauthAuthenticationManager);
		oapf.setAuthenticationManagerDelegator(authenticationManagerBean());
		oapf.setStateless(false);
		http.addFilterBefore(oapf, BasicAuthenticationFilter.class);

		// 开启默认登录页面
		http.authorizeRequests().anyRequest().authenticated().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
			@Override
			public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
				fsi.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
				fsi.setAuthenticationManager(authenticationManagerBean());
				fsi.setAccessDecisionManager(accessDecisionManager());
				return fsi;
			}
		}).and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
				.and()
				//.formLogin().loginPage("/login").successHandler(athenticationSuccessHandler()).permitAll()
				.formLogin().loginPage("/login")
				.successHandler(athenticationSuccessHandler())
				.defaultSuccessUrl("/index")
				.failureUrl("/login?error")
				.and()
				.logout()
				.logoutSuccessUrl("/index")
				.permitAll()
				;

		// 自定义accessDecisionManager访问控制器,并开启表达式语言
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
				.and().authorizeRequests().anyRequest().authenticated().expressionHandler(webSecurityExpressionHandler());

		// @formatter:off
		/*http.authorizeRequests().antMatchers(HttpMethod.OPTIONS,"/oauth/token", "/**").permitAll();
		http.formLogin().loginPage("/login").permitAll()
				.and().requestMatchers().antMatchers("/login", "/oauth/authorize", "/oauth/confirm_access")
				.and().authorizeRequests().anyRequest().authenticated()
				.and().requestMatchers().antMatchers("/mgmt/health")
				.and().authorizeRequests().anyRequest().permitAll();*/
		// @formatter:on

		http.csrf().disable();

		// session管理
		//http.sessionManagement().maximumSessions(1);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
		/*auth.inMemoryAuthentication()
				.withUser("user").password("password").roles("USER")
				.and()
				.withUser("admin").password("admin").roles("ADMIN");
		*/
		//auth.parentAuthenticationManager(authenticationManager);
	}
}

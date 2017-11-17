package com.orrin.sca.common.service.uaa.server.core.config;

import com.orrin.sca.common.service.uaa.server.core.oauth.ClientDetailsServiceImpl;
import com.orrin.sca.common.service.uaa.server.core.secure.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.security.KeyPair;

/**
 * @author orrin.zhang on 2017/7/28.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfiguration.class);

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Autowired
	private ClientDetailsServiceImpl clientDetailsService;

	@Bean
	public RedisTokenStore redisTokenStore(){
		return new RedisTokenStore(redisConnectionFactory);
	}

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		KeyPair keyPair = new KeyStoreKeyFactory(
				new ClassPathResource("keystore.jks"), "foobar".toCharArray())
				.getKeyPair("test");
		converter.setKeyPair(keyPair);
		return converter;
	}

	//可以改成JDBC从库里读或者其他方式。
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		/*
		clients.inMemory()
				.withClient("acme")
				.secret("acmesecret")
				.authorizedGrantTypes("authorization_code", "refresh_token","password")
				.autoApprove(true) //自动跳过确认授权
				.scopes("openid");
		*/
		clients.withClientDetails(clientDetailsService);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)
				.accessTokenConverter(jwtAccessTokenConverter())
				.tokenStore(redisTokenStore())
				.userDetailsService(customUserDetailsService)
		;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
	}
}

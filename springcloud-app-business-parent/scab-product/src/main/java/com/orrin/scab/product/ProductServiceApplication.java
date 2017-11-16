package com.orrin.scab.product;

import com.orrin.sca.component.jpa.dao.BaseJPARepositoryImpl;
import com.orrin.sca.component.jpa.parent.ParentApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author orrin.zhang on 2017/7/28.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableOAuth2Sso
@EnableAutoConfiguration
@EnableHystrix
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableJpaRepositories(repositoryBaseClass = BaseJPARepositoryImpl.class)
public class ProductServiceApplication extends ParentApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}

	/*
	@Autowired
	private OAuth2ClientContext oauth2Context;

	@Autowired
	private OAuth2ProtectedResourceDetails oAuth2ProtectedResourceDetails;

	@Bean
	@LoadBalanced
	OAuth2RestTemplate oAuth2RestTemplate() {
		return new OAuth2RestTemplate(oAuth2ProtectedResourceDetails,oauth2Context);
	}
	*/

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public AlwaysSampler defaultSampler(){
		return new AlwaysSampler();
	}

	@Controller
	@RequestMapping("/")
	public static class DummyController {

		@RequestMapping(method = RequestMethod.GET)
		@ResponseBody
		public String helloWorld(Principal principal, HttpServletRequest request) {
			return principal == null ? "Hello anonymous" : "Hello " + principal.getName();
		}

		@PreAuthorize("#oauth2.hasScope('openid') and hasRole('ROLE_ADMIN')")
		@RequestMapping(value = "secret", method = RequestMethod.GET)
		@ResponseBody
		public String helloSecret(Principal principal) {
			return principal == null ? "Hello anonymous" : "S3CR3T  - Hello " + principal.getName();
		}
	}
}

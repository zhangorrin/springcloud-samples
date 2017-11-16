package com.orrin.sca.component.swagger;

import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

/**
 * @author orrin.zhang on 2017/8/11.
 */
@Configuration
@EnableConfigurationProperties(Swagger2Properties.class)
@EnableSwagger2
public class Swagger2AutoConfiguration {

	private final static Logger logger = LoggerFactory.getLogger(Swagger2AutoConfiguration.class);

	@Autowired
	private Swagger2Properties swagger2Configuration;

	@Bean
	public Docket createRestApi() {

		String basepackage = swagger2Configuration.getDocket().getBasepackage();
		String paths = swagger2Configuration.getDocket().getPaths();
		String groupName = swagger2Configuration.getDocket().getGroupName();
		String pathMapping = swagger2Configuration.getDocket().getPathMapping();

		logger.info("basepackage = {}", basepackage);
		logger.info("paths = {}", paths);
		logger.info("groupName = {}", groupName);
		logger.info("pathMapping = {}", pathMapping);

		Docket docket =  new Docket(DocumentationType.SWAGGER_2)
				.groupName(groupName)
				.apiInfo(apiInfo())
				.pathMapping(pathMapping)// base，最终调用接口后会和paths拼接在一起
				.select()
				.apis(RequestHandlerSelectors.basePackage(basepackage))
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(paths.equals("/*") ? PathSelectors.any() : or(regex(paths)))
				.build();

		return docket;
	}

	private ApiInfo apiInfo() {

		String title = swagger2Configuration.getApiInfo().getTitle();
		String description = swagger2Configuration.getApiInfo().getDescription();
		String termsOfServiceUrl = swagger2Configuration.getApiInfo().getTermsOfServiceUrl();
		String contact_name = swagger2Configuration.getApiInfo().getContact_name();
		String contact_url = swagger2Configuration.getApiInfo().getContact_url();
		String contact_email = swagger2Configuration.getApiInfo().getContact_email();
		String version = swagger2Configuration.getApiInfo().getVersion();

		logger.info("title = {}", title);
		logger.info("description = {}", description);
		logger.info("termsOfServiceUrl = {}", termsOfServiceUrl);
		logger.info("contact_name = {}", contact_name);
		logger.info("contact_url = {}", contact_url);
		logger.info("contact_email = {}", contact_email);
		logger.info("version = {}", version);

		return new ApiInfoBuilder()
				.title(title) //大标题
				.description(description)//详细描述
				.termsOfServiceUrl(StringUtils.isBlank(termsOfServiceUrl) ? "" : termsOfServiceUrl)
				.contact(new Contact(contact_name, contact_url, contact_email))
				.version(version)
				.build();
	}
}

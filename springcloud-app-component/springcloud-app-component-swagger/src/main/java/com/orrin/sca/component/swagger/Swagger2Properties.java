package com.orrin.sca.component.swagger;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author orrin.zhang on 2017/8/11.
 */
@ConfigurationProperties(ignoreUnknownFields = false,prefix = "swagger2")
public class Swagger2Properties {

	private DocketConfiguration docket;

	private ApiInfoConfiguration apiInfo;

	public DocketConfiguration getDocket() {
		return docket;
	}

	public void setDocket(DocketConfiguration docket) {
		this.docket = docket;
	}

	public ApiInfoConfiguration getApiInfo() {
		return apiInfo;
	}

	public void setApiInfo(ApiInfoConfiguration apiInfo) {
		this.apiInfo = apiInfo;
	}

	public static class DocketConfiguration {

		@NotBlank(message = "swagger2.docket.basepackage 配置不能为空")
		private String basepackage;
		@NotBlank(message = "swagger2.docket.paths 配置不能为空")
		private String paths = "*";
		@NotBlank(message = "swagger2.docket.groupName 配置不能为空")
		private String groupName;
		@NotBlank(message = "swagger2.docket.pathMapping 配置不能为空")
		private String pathMapping = "/";

		public String getBasepackage() {
			return basepackage;
		}

		public void setBasepackage(String basepackage) {
			this.basepackage = basepackage;
		}

		public String getPaths() {
			return paths;
		}

		public void setPaths(String paths) {
			this.paths = paths;
		}

		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public String getPathMapping() {
			return pathMapping;
		}

		public void setPathMapping(String pathMapping) {
			this.pathMapping = pathMapping;
		}
	}

	public static class ApiInfoConfiguration {

		@NotBlank(message = "swagger2.apiInfo.title 配置不能为空")
		private String title;
		@NotBlank(message = "swagger2.apiInfo.description 配置不能为空")
		private String description;

		private String termsOfServiceUrl;
		@NotBlank(message = "swagger2.apiInfo.contact_name 配置不能为空")
		private String contact_name;
		@NotBlank(message = "swagger2.apiInfo.contact_url 配置不能为空")
		private String contact_url;
		@NotBlank(message = "swagger2.apiInfo.contact_email 配置不能为空")
		private String contact_email;
		@NotBlank(message = "swagger2.apiInfo.version 配置不能为空")
		private String version;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getTermsOfServiceUrl() {
			return termsOfServiceUrl;
		}

		public void setTermsOfServiceUrl(String termsOfServiceUrl) {
			this.termsOfServiceUrl = termsOfServiceUrl;
		}

		public String getContact_name() {
			return contact_name;
		}

		public void setContact_name(String contact_name) {
			this.contact_name = contact_name;
		}

		public String getContact_url() {
			return contact_url;
		}

		public void setContact_url(String contact_url) {
			this.contact_url = contact_url;
		}

		public String getContact_email() {
			return contact_email;
		}

		public void setContact_email(String contact_email) {
			this.contact_email = contact_email;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}
	}
}

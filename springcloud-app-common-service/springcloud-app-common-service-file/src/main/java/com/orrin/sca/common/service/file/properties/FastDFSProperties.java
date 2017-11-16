package com.orrin.sca.common.service.file.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author orrin.zhang on 2017/8/14.
 */
@ConfigurationProperties(prefix = "fdfs")
public class FastDFSProperties {
	private int connect_timeout = 30;

	private int network_timeout = 30;

	private String charset = "UTF-8";

	private HttpProperties http;

	private List<String> tracker_server;

	public int getConnect_timeout() {
		return connect_timeout;
	}

	public void setConnect_timeout(String connect_timeout) {
		this.connect_timeout = Integer.parseInt(connect_timeout);
	}

	public int getNetwork_timeout() {
		return network_timeout;
	}

	public void setNetwork_timeout(String network_timeout) {
		this.network_timeout = Integer.parseInt(network_timeout);
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public HttpProperties getHttp() {
		return http;
	}

	public void setHttp(HttpProperties http) {
		this.http = http;
	}

	public List<String> getTracker_server() {
		return tracker_server;
	}

	public void setTracker_server(List<String> tracker_server) {
		this.tracker_server = tracker_server;
	}

	public static class HttpProperties {

		private int tracker_http_port = 80;

		private boolean anti_steal_token;

		private String secret_key;

		public int getTracker_http_port() {
			return tracker_http_port;
		}

		public void setTracker_http_port(String tracker_http_port) {
			this.tracker_http_port = Integer.parseInt(tracker_http_port);
		}

		public boolean getAnti_steal_token() {
			return anti_steal_token;
		}

		public void setAnti_steal_token(String anti_steal_token) {
			this.anti_steal_token = Boolean.parseBoolean(anti_steal_token);
		}

		public String getSecret_key() {
			return secret_key;
		}

		public void setSecret_key(String secret_key) {
			this.secret_key = secret_key;
		}

		@Override
		public String toString() {
			return "HttpProperties{" +
					"tracker_http_port=" + tracker_http_port +
					", anti_steal_token=" + anti_steal_token +
					", secret_key='" + secret_key + '\'' +
					'}';
		}
	}

	@Override
	public String toString() {
		return "FastDFSProperties{" +
				"connect_timeout=" + connect_timeout +
				", network_timeout=" + network_timeout +
				", charset='" + charset + '\'' +
				", http=" + http == null ? "null" : http.toString() +
				", tracker_server=" + tracker_server +
				'}';
	}
}

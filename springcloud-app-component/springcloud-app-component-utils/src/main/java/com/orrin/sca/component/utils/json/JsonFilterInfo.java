package com.orrin.sca.component.utils.json;

import java.io.Serializable;

/**
 * @author orrin.zhang on 2017/8/4.
 */
public class JsonFilterInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Class<?> type;
	private String include;
	private String filter;

	public JsonFilterInfo() {
	}

	public JsonFilterInfo(Class<?> type, String include, String filter) {
		this.type = type;
		this.include = include;
		this.filter = filter;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public String getInclude() {
		return include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}
}

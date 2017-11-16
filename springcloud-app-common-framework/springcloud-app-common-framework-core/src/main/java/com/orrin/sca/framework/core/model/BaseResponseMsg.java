package com.orrin.sca.framework.core.model;

import java.io.Serializable;

/**
 * @author orrin.zhang on 2017/8/4.
 */
public class BaseResponseMsg implements Serializable {

	private static final long serialVersionUID = 801850187875714324L;

	private String responseCode;//返回码
	private String responseMsg;//返回结果

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
}

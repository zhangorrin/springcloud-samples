package com.orrin.sca.framework.core.model;

/**
 * @author orrin.zhang on 2017/8/4.
 */
public class ResponseResult<T> extends BaseResponseMsg{
	private static final long serialVersionUID = 1L;
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}

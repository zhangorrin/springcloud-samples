package com.orrin.sca.framework.core.exception;

/**
 * @author orrin.zhang on 2017/8/4.
 */
public class BusinessException extends BaseException {
	private static final long serialVersionUID = -8801952505203103469L;

	private BusinessException() {
	}

	public BusinessException(String errorCode) {
		this.errorCode = errorCode;
	}

	public BusinessException(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}
}

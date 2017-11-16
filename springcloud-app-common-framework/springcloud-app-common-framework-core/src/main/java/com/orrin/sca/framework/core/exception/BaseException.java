package com.orrin.sca.framework.core.exception;

/**
 * @author orrin.zhang on 2017/8/4.
 */
public class BaseException extends RuntimeException {

	/**  */
	private static final long serialVersionUID = 1L;

	protected String errorCode;

	protected String message;

	public BaseException() {
		super();
	}

	public BaseException(String errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public BaseException(Throwable arg0) {
		super(arg0);
	}

	public BaseException(String errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public BaseException(String errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}

	public BaseException(String errorCode, String message, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		this.message = message;
	}

	/**
	 * Getter method for property <tt>errorCode</tt>.
	 *
	 * @return property value of errorCode
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Setter method for property <tt>errorCode</tt>.
	 *
	 * @param errorCode value to be assigned to property errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Getter method for property <tt>message</tt>.
	 *
	 * @return property value of message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter method for property <tt>message</tt>.
	 *
	 * @param message value to be assigned to property message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}

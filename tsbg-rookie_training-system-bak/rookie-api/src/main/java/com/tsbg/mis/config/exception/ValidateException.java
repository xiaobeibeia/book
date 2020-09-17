package com.tsbg.mis.config.exception;

/**
 * 表单验证异常，此异常不写日志，直接返回前台
 * @author 海波
 *
 */
public class ValidateException extends BaseRuntimeException {

	private static final long serialVersionUID = -959458039939731887L;

	public ValidateException() {
		super();
	}

	public ValidateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidateException(String message) {
		super(message);
	}

	public ValidateException(Throwable cause) {
		super(cause);
	}
	
}

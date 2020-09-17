package com.tsbg.mis.config.exception;

public class FormatException extends BaseRuntimeException {

	private static final long serialVersionUID = -3226743937651967907L;

	public FormatException() {
		super();
	}

	public FormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FormatException(String message, Throwable cause) {
		super(message, cause);
	}

	public FormatException(String message) {
		super(message);
	}

	public FormatException(Throwable cause) {
		super(cause);
	}

}

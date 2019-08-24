package com.its.common.exception;

public class NotDeletedException extends ServiceException {

	private static final long serialVersionUID = -7419811878588715532L;

	public NotDeletedException() {
		super();
	}

	public NotDeletedException(String message) {
		super(message);
	}

	public NotDeletedException(Throwable cause) {
		super(cause);
	}

	public NotDeletedException(String message, Throwable cause) {
		super(message, cause);
	}
}

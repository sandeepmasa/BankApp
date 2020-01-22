package com.bank.service.exception;

/**
 * This Exception thrown when there is a Insufficient balance in the customer account
 * Example withdrawal limit exceeds, Daily limit exceeds.
 *
 * @author : Sandeep
 */

public class InsufficientBalanceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientBalanceException(String msg) {
		super(msg);
	}

	public InsufficientBalanceException(String msg, Throwable cause) {
		super(msg, cause);
	}
}

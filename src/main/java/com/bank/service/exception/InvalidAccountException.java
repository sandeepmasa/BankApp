package com.bank.service.exception;

/**
 * Custom exception thrown when Invalid Account found or not accessible.
 *
 * @author : Sandeep M
 */

public class InvalidAccountException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidAccountException(String msg) {
        super(msg);
    }

    public InvalidAccountException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

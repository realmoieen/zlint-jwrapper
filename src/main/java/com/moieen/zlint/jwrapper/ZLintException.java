package com.moieen.zlint.jwrapper;

/**
 * Exception class used to throw any kind of exception while linting the certificate
 */
public class ZLintException extends Exception {
    public ZLintException() {
    }

    public ZLintException(String message) {
        super(message);
    }

    public ZLintException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZLintException(Throwable cause) {
        super(cause);
    }

    public ZLintException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

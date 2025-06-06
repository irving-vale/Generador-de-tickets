package com.joirv.CursoSpringBoot.infraestructure.exceptions.personalExceptions;

public class CustomerCounterException extends RuntimeException {

    public CustomerCounterException(String message) {
        super(message);
    }

    public CustomerCounterException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerCounterException(Throwable cause) {
        super(cause);
    }

    public CustomerCounterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

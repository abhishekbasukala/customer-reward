package com.coding.task.rewards.exception;

import org.springframework.http.HttpStatus;

public class RewardsException extends RuntimeException {

    private HttpStatus httpStatus;

    public RewardsException() {
        super();
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public RewardsException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}

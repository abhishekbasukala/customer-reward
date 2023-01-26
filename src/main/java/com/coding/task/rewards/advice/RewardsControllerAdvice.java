package com.coding.task.rewards.advice;

import com.coding.task.rewards.exception.RewardsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RewardsControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RewardsException.class})
    protected ResponseEntity<Object> handleRewardsException(RewardsException ex, WebRequest request) {
        String message = ex.getMessage();
        log.error(message);
        return handleExceptionInternal(ex, message, new HttpHeaders(), ex.getHttpStatus(), request);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<Object> handleInternalServerError(Exception ex, WebRequest request) {
        String message = "Unknown error occurred. Please contact the app support team.";
        log.error(message);
        return handleExceptionInternal(ex, message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}

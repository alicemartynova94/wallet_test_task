package com.wallet.wallet_test_task.exception_handling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
@Slf4j
public class WalletGlobalExceptionHandler {
    @ExceptionHandler(IncorrectAmountException.class)
    public ResponseEntity<Object> handleIncorrectAmountException(IncorrectAmountException exception) {
        log.error("IncorrectAmountException occurred: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Object> handleInsufficientFundsException(InsufficientFundsException exception) {
        log.error("InsufficientFundsException occurred: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler(InvalidOperationArgumentException.class)
    public ResponseEntity<Object> handleInvalidOperationArgumentException(InvalidOperationArgumentException exception) {
        log.error("InvalidOperationArgumentException occurred: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    @ExceptionHandler(NoSuchWalletException.class)
    public ResponseEntity<Object> handleNoSuchWalletException(NoSuchWalletException exception) {
        log.error("NoSuchWalletException occurred: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    @ExceptionHandler(WalletDeletedException.class)
    public ResponseEntity<Object> handleWalletDeletedException(WalletDeletedException exception) {
        log.error("WalletDeletedException occurred: {}", exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.GONE)
                .body(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidJson(HttpMessageNotReadableException exception) {
        String message = "JSON format is invalid.";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<Object> handleInternalServerError() {
        String message = "Server is unable to proses the request.";
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(message);
    }

    @ExceptionHandler(HttpServerErrorException.BadGateway.class)
    public ResponseEntity<Object> handleBadGatewayError() {
        String message = "Your request cannot be processed due to network issues or because database is unreachable.";
        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(message);
    }

    @ExceptionHandler(HttpServerErrorException.ServiceUnavailable.class)
    public ResponseEntity<Object> handleServiceUnavailableError() {
        String message = "The server is temporarily unavailable.";
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(message);
    }

    @ExceptionHandler(HttpServerErrorException.GatewayTimeout.class)
    public ResponseEntity<Object> handleGatewayTimeoutError() {
        String message = "The server did not receive a timely response from external resource.";
        return ResponseEntity
                .status(HttpStatus.GATEWAY_TIMEOUT)
                .body(message);
    }

}

package com.wallet.wallet_test_task.exception_handling;

public class IncorrectAmountException extends RuntimeException {
    public IncorrectAmountException(String message) {
        super(message);
    }
}

package com.wallet.wallet_test_task.exception_handling;

public class NoSuchWalletException extends RuntimeException {
    public NoSuchWalletException(String message) {
        super(message);
    }
}

package com.wallet.wallet_test_task.exception_handling;

public class WalletDeletedException extends RuntimeException {
    public WalletDeletedException(String message) {
        super(message);
    }
}

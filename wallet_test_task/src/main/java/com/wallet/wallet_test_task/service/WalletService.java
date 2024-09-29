package com.wallet.wallet_test_task.service;


import com.wallet.wallet_test_task.dto.WalletDto;

import java.util.UUID;

public interface WalletService {
    public void updateWalletByDepositingOrWithdrawing(WalletDto walletDto);

    public Double getWalletBalance(UUID walletId);

    public void softDelete(UUID walletId);
}

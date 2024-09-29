package com.wallet.wallet_test_task.controller;

import com.wallet.wallet_test_task.dto.WalletDto;
import com.wallet.wallet_test_task.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/wallet")
    public void updateWalletByDepositingOrWithdrawing(@RequestBody WalletDto walletDto) {
        walletService.updateWalletByDepositingOrWithdrawing(walletDto);
    }

    @GetMapping("/wallets/{walletId}")
    public Double getWalletBalance(@PathVariable UUID walletId) {
        return walletService.getWalletBalance(walletId);
    }

    @DeleteMapping("/wallets/{walletId}")
    public void softDelete(@PathVariable UUID walletId) {
        walletService.softDelete(walletId);
    }

}

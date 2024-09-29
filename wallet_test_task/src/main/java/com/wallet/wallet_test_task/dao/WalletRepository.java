package com.wallet.wallet_test_task.dao;


import com.wallet.wallet_test_task.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    @Query("SELECT w FROM Wallet w WHERE w.id = :id AND w.walletDeletedTime IS NULL")
    Optional<Wallet> findByIdActiveWallet(@Param("id") UUID id);
}

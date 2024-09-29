package com.wallet.wallet_test_task.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "available_funds")
    private Double availableFunds;
    @CreationTimestamp
    @Column(name = "wallet_creation_time")
    private LocalDateTime walletCreationTime;
    @UpdateTimestamp
    @Column(name = "wallet_updated_time")
    private LocalDateTime walletUpdatedTime;
    @Column(name = "wallet_deleted_time")
    private LocalDateTime walletDeletedTime;
}

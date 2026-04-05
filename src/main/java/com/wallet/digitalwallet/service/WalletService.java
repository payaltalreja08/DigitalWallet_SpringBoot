package com.wallet.digitalwallet.service;

import com.wallet.digitalwallet.entity.Wallet;
import com.wallet.digitalwallet.exception.ResourceNotFoundException;
import com.wallet.digitalwallet.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class WalletService {
    private static final Logger log = LoggerFactory.getLogger(WalletService.class);

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public BigDecimal getBalance(Long userId) {
        log.debug("Tracing: Getting balance for user {}", userId);
        return walletRepository.findByUserId(userId)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user ID: " + userId));
    }

    @Transactional
    public void addMoney(Long userId, BigDecimal amount) {
        log.debug("Tracing: Adding {} to wallet of user {}", amount, userId);
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user ID: " + userId));

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Validation failure: Attempted to add non-positive amount {} to user {}", amount, userId);
            throw new IllegalArgumentException("Amount must be positive");
        }

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        log.info("Successfully added {} to wallet of user {}", amount, userId);
    }

    @Transactional
    public void updateBalance(Wallet wallet) {
        walletRepository.save(wallet);
    }
}

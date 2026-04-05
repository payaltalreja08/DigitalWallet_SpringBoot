package com.wallet.digitalwallet.service;

import com.wallet.digitalwallet.dto.SendMoneyRequest;
import com.wallet.digitalwallet.entity.Transaction;
import com.wallet.digitalwallet.entity.Wallet;
import com.wallet.digitalwallet.exception.InsufficientBalanceException;
import com.wallet.digitalwallet.exception.InvalidRequestException;
import com.wallet.digitalwallet.repository.TransactionRepository;
import com.wallet.digitalwallet.repository.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository) {
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public Transaction sendMoney(Long senderId, SendMoneyRequest request) {
        log.debug("Tracing: Starting transaction from user {} to user {} for amount {}", 
                  senderId, request.getReceiverId(), request.getAmount());

        if (senderId.equals(request.getReceiverId())) {
            log.warn("Validation failure: User {} attempted to send money to themselves", senderId);
            throw new InvalidRequestException("Cannot send money to yourself");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Validation failure: Attempted to send non-positive amount {}", request.getAmount());
            throw new InvalidRequestException("Amount must be greater than zero");
        }

        Wallet senderWallet = walletRepository.findByUserId(senderId)
                .orElseThrow(() -> new InvalidRequestException("Sender wallet not found"));
        Wallet receiverWallet = walletRepository.findByUserId(request.getReceiverId())
                .orElseThrow(() -> new InvalidRequestException("Receiver wallet not found"));

        if (senderWallet.getBalance().compareTo(request.getAmount()) < 0) {
            log.warn("Validation failure: User {} has insufficient balance {}", senderId, senderWallet.getBalance());
            throw new InsufficientBalanceException("Insufficient balance in wallet");
        }

        // Processing transaction
        senderWallet.setBalance(senderWallet.getBalance().subtract(request.getAmount()));
        receiverWallet.setBalance(receiverWallet.getBalance().add(request.getAmount()));

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        Transaction transaction = Transaction.builder()
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .amount(request.getAmount())
                .status(Transaction.TransactionStatus.SUCCESS)
                .timestamp(LocalDateTime.now())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Successfully completed transaction ID: {}", savedTransaction.getId());
        return savedTransaction;
    }

    public List<Transaction> getHistory(Long userId) {
        log.debug("Tracing: Fetching transaction history for user {}", userId);
        return transactionRepository.findAllBySenderIdOrReceiverIdOrderByTimestampDesc(userId, userId);
    }
}

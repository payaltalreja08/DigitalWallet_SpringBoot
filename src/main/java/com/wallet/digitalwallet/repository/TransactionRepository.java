package com.wallet.digitalwallet.repository;

import com.wallet.digitalwallet.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllBySenderIdOrReceiverIdOrderByTimestampDesc(Long senderId, Long receiverId);
    
    List<Transaction> findAllBySenderId(Long senderId);
}

package com.wallet.digitalwallet.service;

import com.wallet.digitalwallet.entity.Transaction;
import com.wallet.digitalwallet.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {
    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final TransactionRepository transactionRepository;

    public AnalyticsService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal getTotalSpent(Long userId) {
        log.debug("Tracing: Calculating total spent for user {}", userId);
        return transactionRepository.findAllBySenderId(userId).stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<Month, BigDecimal> getMonthlySpending(Long userId) {
        log.debug("Tracing: Grouping monthly spending for user {}", userId);
        List<Transaction> transactions = transactionRepository.findAllBySenderId(userId).stream()
                .filter(t -> t.getStatus() == Transaction.TransactionStatus.SUCCESS)
                .collect(Collectors.toList());

        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getTimestamp().getMonth(),
                        () -> new TreeMap<>(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));
    }
}

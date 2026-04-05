package com.wallet.digitalwallet.service;

import com.wallet.digitalwallet.dto.SendMoneyRequest;
import com.wallet.digitalwallet.entity.Transaction;
import com.wallet.digitalwallet.entity.Wallet;
import com.wallet.digitalwallet.exception.InsufficientBalanceException;
import com.wallet.digitalwallet.repository.TransactionRepository;
import com.wallet.digitalwallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMoney_Success() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        Wallet senderWallet = Wallet.builder().id(1L).balance(new BigDecimal("500.00")).build();
        Wallet receiverWallet = Wallet.builder().id(2L).balance(new BigDecimal("200.00")).build();

        SendMoneyRequest request = new SendMoneyRequest();
        request.setReceiverId(receiverId);
        request.setAmount(amount);

        when(walletRepository.findByUserId(senderId)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByUserId(receiverId)).thenReturn(Optional.of(receiverWallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Transaction result = transactionService.sendMoney(senderId, request);

        // Assert
        assertNotNull(result);
        assertEquals(Transaction.TransactionStatus.SUCCESS, result.getStatus());
        assertEquals(new BigDecimal("400.00"), senderWallet.getBalance());
        assertEquals(new BigDecimal("300.00"), receiverWallet.getBalance());
        verify(walletRepository, times(1)).save(senderWallet);
        verify(walletRepository, times(1)).save(receiverWallet);
    }

    @Test
    void testSendMoney_InsufficientBalance() {
        // Arrange
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal amount = new BigDecimal("1000.00");

        Wallet senderWallet = Wallet.builder().id(1L).balance(new BigDecimal("500.00")).build();
        Wallet receiverWallet = Wallet.builder().id(2L).balance(new BigDecimal("200.00")).build();

        SendMoneyRequest request = new SendMoneyRequest();
        request.setReceiverId(receiverId);
        request.setAmount(amount);

        when(walletRepository.findByUserId(senderId)).thenReturn(Optional.of(senderWallet));
        when(walletRepository.findByUserId(receiverId)).thenReturn(Optional.of(receiverWallet));

        // Act & Assert
        assertThrows(InsufficientBalanceException.class, () -> transactionService.sendMoney(senderId, request));
        verify(transactionRepository, never()).save(any());
    }
}

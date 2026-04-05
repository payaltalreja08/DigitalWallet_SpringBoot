package com.wallet.digitalwallet.controller;

import com.wallet.digitalwallet.dto.SendMoneyRequest;
import com.wallet.digitalwallet.entity.Transaction;
import com.wallet.digitalwallet.security.UserDetailsImpl;
import com.wallet.digitalwallet.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMoney(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @Valid @RequestBody SendMoneyRequest request) {
        Transaction transaction = transactionService.sendMoney(userDetails.getId(), request);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getHistory(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(transactionService.getHistory(userDetails.getId()));
    }
}

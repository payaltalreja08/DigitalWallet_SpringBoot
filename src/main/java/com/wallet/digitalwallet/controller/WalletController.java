package com.wallet.digitalwallet.controller;

import com.wallet.digitalwallet.dto.AddMoneyRequest;
import com.wallet.digitalwallet.security.UserDetailsImpl;
import com.wallet.digitalwallet.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(walletService.getBalance(userDetails.getId()));
    }

    @PostMapping("/add-money")
    public ResponseEntity<?> addMoney(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @Valid @RequestBody AddMoneyRequest request) {
        walletService.addMoney(userDetails.getId(), request.getAmount());
        return ResponseEntity.ok("Money added successfully!");
    }
}

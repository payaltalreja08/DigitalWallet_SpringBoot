package com.wallet.digitalwallet.controller;

import com.wallet.digitalwallet.security.UserDetailsImpl;
import com.wallet.digitalwallet.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.time.Month;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/total-spent")
    public ResponseEntity<BigDecimal> getTotalSpent(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(analyticsService.getTotalSpent(userDetails.getId()));
    }

    @GetMapping("/monthly-spending")
    public ResponseEntity<Map<Month, BigDecimal>> getMonthlySpending(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(analyticsService.getMonthlySpending(userDetails.getId()));
    }
}

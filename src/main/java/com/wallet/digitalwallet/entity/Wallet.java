package com.wallet.digitalwallet.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallets")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    public Wallet() {}

    public Wallet(Long id, User user, BigDecimal balance) {
        this.id = id;
        this.user = user;
        this.balance = balance;
    }

    public static WalletBuilder builder() {
        return new WalletBuilder();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public static class WalletBuilder {
        private Long id;
        private User user;
        private BigDecimal balance;

        public WalletBuilder id(Long id) { this.id = id; return this; }
        public WalletBuilder user(User user) { this.user = user; return this; }
        public WalletBuilder balance(BigDecimal balance) { this.balance = balance; return this; }
        public Wallet build() { return new Wallet(id, user, balance); }
    }
}

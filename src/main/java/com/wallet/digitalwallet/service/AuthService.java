package com.wallet.digitalwallet.service;

import com.wallet.digitalwallet.dto.JwtResponse;
import com.wallet.digitalwallet.dto.LoginRequest;
import com.wallet.digitalwallet.dto.SignupRequest;
import com.wallet.digitalwallet.entity.User;
import com.wallet.digitalwallet.entity.Wallet;
import com.wallet.digitalwallet.repository.UserRepository;
import com.wallet.digitalwallet.security.JwtUtils;
import com.wallet.digitalwallet.security.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public void signup(SignupRequest request) {
        log.debug("Tracing: Starting signup process for email {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Validation failed: User already exists with email {}", request.getEmail());
            throw new RuntimeException("Error: Email is already in use!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // Create initial wallet
        Wallet wallet = Wallet.builder()
                .user(user)
                .balance(BigDecimal.ZERO)
                .build();
        user.setWallet(wallet);

        userRepository.save(user);
        log.info("Successfully signed up user: {}", request.getEmail());
    }

    public JwtResponse login(LoginRequest request) {
        log.debug("Tracing: Authenticating user {}", request.getEmail());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        log.info("Successfully logged in user: {}", request.getEmail());
        return new JwtResponse(jwt, userDetails.getId(), userDetails.getName(), userDetails.getEmail());
    }
}

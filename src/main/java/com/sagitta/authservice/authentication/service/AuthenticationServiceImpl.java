package com.sagitta.authservice.authentication.service;

import com.sagitta.authservice.authentication.domain.AccessToken;
import com.sagitta.authservice.authentication.domain.AccessTokenRepository;
import com.sagitta.authservice.authentication.domain.Account;
import com.sagitta.authservice.authentication.domain.AccountRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;

    private final AccessTokenRepository accessTokenRepository;

    public AuthenticationServiceImpl(PasswordEncoder passwordEncoder, AccountRepository accountRepository, AccessTokenRepository accessTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.accessTokenRepository = accessTokenRepository;
    }


    @Override
    public ResponseEntity<String> login(String etin, String password) {
        Optional<Account> optionalAccount = accountRepository.findByEtin(etin);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Account account = optionalAccount.get();
        if (!passwordEncoder.matches(password, account.getPassword())) {
            return ResponseEntity.badRequest().body("Incorrect password");
        }
        if (passwordEncoder.matches(password, account.getPassword())) {
            account.setLastLogin(new Date());
            account.setActive(true);
            String token = Jwts.builder().setSubject(etin).signWith(key).compact();
            account.setAccessToken(token);
            accountRepository.save(account);
            AccessToken accessToken = AccessToken.builder()
                    .accessToken(token)
                    .isActive(true)
                    .build();
            accessTokenRepository.save(accessToken);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.ok("User logged in successfully");
    }


    @Override
    public ResponseEntity<String> signup(String etin, String password) {

        if (accountRepository.findByEtin(etin).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists");
        }
        Account user = Account.builder()
                .etin(etin)
                .password(passwordEncoder.encode(password))
                .build();

        // Hash the user's password before storing it in the database
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        accountRepository.save(user);
        return ResponseEntity.ok(this.login(etin, password).getBody());
    }

    public ResponseEntity<String> logout(String etin, String accessToken) {
        Optional<Account> optionalAccount = accountRepository.findByEtin(etin);
        if (optionalAccount.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        Account account = optionalAccount.get();
        account.setActive(false);
        account.setAccessToken(null);
        accountRepository.save(account);
        return ResponseEntity.ok("User logged out successfully");
    }
}

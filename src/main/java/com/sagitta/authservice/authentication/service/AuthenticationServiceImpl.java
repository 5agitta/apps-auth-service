package com.sagitta.authservice.authentication.service;

import com.sagitta.authservice.authentication.domain.AccessToken;
import com.sagitta.authservice.authentication.domain.AccessTokenRepository;
import com.sagitta.authservice.authentication.domain.Account;
import com.sagitta.authservice.authentication.domain.AccountRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
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
    public String login(String etin, String password) {
        Optional<Account> optionalAccount = accountRepository.findByEtin(etin);
        if (optionalAccount.isEmpty()) {
            return "User not found";
        }
        Account account = optionalAccount.get();
        if (!passwordEncoder.matches(password, account.getPassword())) {
            return "Incorrect password";
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
            return token;
        }
        return "Login failed";
    }


    @Override
    public String signup(String etin, String password) {

        if (accountRepository.findByEtin(etin).isPresent()) {
            return "User already exists";
        }
        Account user = Account.builder()
                .etin(etin)
                .password(passwordEncoder.encode(password))
                .build();

        // Hash the user's password before storing it in the database
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        accountRepository.save(user);
        return this.login(etin, password);
    }

    public String logout(String etin, String accessToken) {
        Optional<Account> optionalAccount = accountRepository.findByEtin(etin);
        if (optionalAccount.isEmpty()) {
            return "User not found";
        }
        Account account = optionalAccount.get();
        account.setActive(false);
        account.setAccessToken(null);
        accountRepository.save(account);
        return "User logged out successfully";
    }
}

package com.example.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
@Transactional
public class AccountService {
    
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account){
        if(account.getUsername().isEmpty() || account.getPassword().length() < 4){
            throw new IllegalArgumentException("Invalid input");
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            throw new DataIntegrityViolationException("Username already exists");
        }

        return accountRepository.save(account);
    }

    public Account loginAccount(String username, String password){
        return accountRepository.findByUsernameAndPassword(username, password)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));
        
    }

}

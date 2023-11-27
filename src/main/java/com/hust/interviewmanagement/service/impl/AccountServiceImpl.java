package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Account;
import com.hust.interviewmanagement.repository.AccountRepository;
import com.hust.interviewmanagement.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    final private AccountRepository accountRepository;
    @Override
    public Optional<Account> findAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }
}

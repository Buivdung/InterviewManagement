package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> findAccountByEmail(String email);
}

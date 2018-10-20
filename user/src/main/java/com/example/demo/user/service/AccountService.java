package com.example.demo.user.service;

import com.example.demo.user.domain.Account;
import com.example.demo.user.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    /*
     * Input Type	: String
     * Output Type	: Account
     * Description	: accountName을 통해 Account 정보를 조회하는 Service를 구현
     */
    public Account selectAccount(String accountName) {
        return this.accountRepository.findByAccountName(accountName);
    }
}
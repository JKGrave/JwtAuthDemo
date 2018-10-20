package com.example.demo.user.controller;

import com.example.demo.user.domain.Account;
import com.example.demo.user.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    /*
     * Input Type	: String
     * Output Type	: ResponseEntity<Account>
     * Description	: /account/{accountName}로 들어온 요청을 기반으로 해당 accountName이 존재하는지 검색하고
     * 				    그 결과를 리턴한다.
     */
    @RequestMapping("/account/{accountName}")
    @ResponseBody
    public ResponseEntity<Account> getAccount(@PathVariable String accountName){
        ResponseEntity<Account> re = new ResponseEntity<>(accountService.selectAccount(accountName), HttpStatus.OK);
        return re;
    }
}
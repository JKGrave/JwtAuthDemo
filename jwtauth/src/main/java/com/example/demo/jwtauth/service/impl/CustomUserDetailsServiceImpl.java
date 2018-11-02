package com.example.demo.jwtauth.service.impl;

import com.example.demo.jwtauth.domain.Account;
import com.example.demo.jwtauth.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpEntity<Account> entity = restTemplate.getForEntity("http://user/account/" + username, Account.class);

        return entity.getBody();
    }

}

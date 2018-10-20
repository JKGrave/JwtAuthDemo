package com.example.demo.client;

import com.example.demo.client.domain.Client;
import com.example.demo.client.respository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ClientApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void run(String... args) {
        Client c1 = new Client();
        c1.setAccessTokenValiditySeconds(3000);
        c1.setRefreshTokenValiditySeconds(3000);
        c1.setAuthorities("CLIENT,ROLE_CLIENT");
        c1.setClientId("foo");
        c1.setClientSecret(new BCryptPasswordEncoder().encode("bar"));
        c1.setScope("read");
        c1.setAuthorizedGrantTypes("client_credentials,password,authorization_code");
        c1.setRegisteredRedirectUris("");
        this.clientRepository.save(c1);


        Client c2 = new Client();
        c2.setAccessTokenValiditySeconds(3000);
        c2.setRefreshTokenValiditySeconds(3000);
        c2.setAuthorities("CLIENT,ROLE_CLIENT");
        c2.setClientId("api");
        c2.setClientSecret(new BCryptPasswordEncoder().encode("apipassword"));
        c2.setScope("read");
        c2.setAuthorizedGrantTypes("client_credentials,password,authorization_code");
        c2.setRegisteredRedirectUris("");
        this.clientRepository.save(c2);
    }
}

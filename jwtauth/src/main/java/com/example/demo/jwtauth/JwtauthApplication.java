package com.example.demo.jwtauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// https://codeburst.io/jwt-to-authenticate-servers-apis-c6e179aa8c4e
@SpringBootApplication
@EnableDiscoveryClient
public class JwtauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtauthApplication.class, args);
    }
}

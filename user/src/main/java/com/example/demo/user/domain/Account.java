package com.example.demo.user.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @GeneratedValue(strategy=GenerationType.AUTO)
    private long seq;

    @Id
    @Column(length = 20)
    private String accountName;

    @Column(length = 100)
    private String accountPass;

    public Account(String accountName, String accountPass) {
        this.accountName = accountName;
        this.accountPass = accountPass;
    }
}
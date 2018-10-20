package com.example.demo.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(length = 20)
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
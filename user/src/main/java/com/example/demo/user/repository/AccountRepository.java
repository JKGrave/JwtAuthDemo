package com.example.demo.user.repository;

import com.example.demo.user.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
 * Description : 스프링 데이터 리포지토리를 사용하여 accountName 기반으로 Account 정보를 조회할 수 있도록 한다.
 */
@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, String>{
    Account findByAccountName(String accountName);
}
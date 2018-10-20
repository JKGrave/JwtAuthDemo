package com.example.demo.client.respository;

import com.example.demo.client.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
 * Description : 스프링 데이터 리포지토리를 사용하여 clientId를 기반으로 Client 정보를 조회할 수 있도록 한다.
 */
@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, String> {
    Client findByClientId(String clientId);
}
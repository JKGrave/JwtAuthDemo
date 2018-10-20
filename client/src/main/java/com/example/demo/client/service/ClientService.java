package com.example.demo.client.service;

import com.example.demo.client.domain.Client;
import com.example.demo.client.respository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    /*
     * Input Type	: String
     * Output Type	: Client
     * Description	: clientId를 통해 Client 정보를 조회하는 Service를 구현
     */
    public Client selectClient(String clientId) {
        return this.clientRepository.findByClientId(clientId);
    }
}

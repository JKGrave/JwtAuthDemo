package com.example.demo.client.controller;

import com.example.demo.client.domain.Client;
import com.example.demo.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    /*
     * Input Type	: String
     * Output Type	: ResponseEntity<Client>
     * Description	: /client/{clientId}로 들어온 요청을 기반으로 해당 clientId가 존재하는지 검색하고
     * 				    그 결과를 리턴한다.
     */
    @RequestMapping("/client/{clientId}")
    @ResponseBody
    public ResponseEntity<Client> getClient(@PathVariable String clientId){
        ResponseEntity<Client> re = new ResponseEntity<>(this.clientService.selectClient(clientId), HttpStatus.OK);
        return re;
    }
}

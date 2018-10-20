package com.example.demo.jwtauth.service.impl;

import com.example.demo.jwtauth.domain.Client;
import com.example.demo.jwtauth.service.CustomClientDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomClientDetailsServiceImpl implements CustomClientDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomClientDetailsServiceImpl.class);

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        this.logger.info("client id equals: " + clientId);

        HttpEntity<Client> entity = this.restTemplate.getForEntity("http://localhost:8083/client/" + clientId, Client.class);
        BaseClientDetails baseClientDetails =
                new BaseClientDetails(entity.getBody().getClientId(), entity.getBody().getResourceIds(),
                        entity.getBody().getScope(), entity.getBody().getAuthorizedGrantTypes(),
                        entity.getBody().getAuthorities(), entity.getBody().getRegisteredRedirectUris());

        baseClientDetails.setClientSecret(entity.getBody().getClientSecret());

        this.logger.info(baseClientDetails.toString());

        return baseClientDetails;
    }

}

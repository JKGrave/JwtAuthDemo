package com.example.demo.front.controller;

import com.example.demo.front.config.FrontConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/login")
public class AuthorizationController {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    RestTemplate authRestTemplate = new RestTemplate();

    @Autowired
    private FrontConfig frontConfig;

    // @Consumes : 수신 하고자하는 데이터 포맷을 정의한다.
    // @Produces : 출력하고자 하는 데이터 포맷을 정의한다.
    @RequestMapping(value = "", method = RequestMethod.POST
            ,consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.TEXT_PLAIN_VALUE}
//            ,produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.TEXT_PLAIN_VALUE}
            )
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", "password");
        map.add("scope", "read");

        this.logger.info("username: " + username + ", password: " + password);
        this.logger.info("clientId: " + this.frontConfig.getClientId() + ", clientSecret: " + this.frontConfig.getClientSecret());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Basic Auth
        String plainCredentials = this.frontConfig.getClientId() + ":" + this.frontConfig.getClientSecret();
        String base64ClientCredentials = new String(Base64Utils.encode(plainCredentials.getBytes()));
        httpHeaders.add("Authorization", "Basic " + base64ClientCredentials);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);

        this.logger.info(request.toString());

        ResponseEntity<String> responseEntity = this.authRestTemplate

                .exchange(
                "http://localhost:8080/oauth/token",
                HttpMethod.POST,
                request,
                String.class);
        this.logger.info("return value: " + responseEntity.toString());
        return responseEntity.toString();
    }
}

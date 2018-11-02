package com.example.demo.front.controller;

import com.example.demo.front.config.FrontConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/login")
public class AuthorizationController {

    private static Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    @LoadBalanced
    @Autowired
    private RestTemplate authRestTemplate;

    @Autowired
    private FrontConfig frontConfig;

    @RequestMapping("/me")
    public String getPrincipal(@RequestHeader("Authorization") String token) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", token);

        this.logger.info("Authorization: " + token);

        HttpEntity request = new HttpEntity(httpHeaders);
        HttpEntity<String> p = this.authRestTemplate.exchange("http://api-gateway/auth/me",
                HttpMethod.GET,
                request,
                String.class)
                ;

        if (!p.hasBody())
            return "Not Authorized";
        else
            return p.getBody();
    }

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

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Basic Auth
        String plainCredentials = this.frontConfig.getClientId() + ":" + this.frontConfig.getClientSecret();
        String base64ClientCredentials = new String(Base64Utils.encode(plainCredentials.getBytes()));
        httpHeaders.add("Authorization", "Basic " + base64ClientCredentials);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);

        this.logger.info(
                "Base 64 Client Credentials: " + base64ClientCredentials
                + "username: " + username + ", password: " + password
                + ", clientId: " + this.frontConfig.getClientId()
                + ", clientSecret: " + this.frontConfig.getClientSecret()
        );

        String returnString = null;

//        try {
        ResponseEntity<String> responseEntity = this.authRestTemplate
                .exchange(
                        "http://jwtauth/oauth/token",//"http://localhost:8080/oauth/token",
                        HttpMethod.POST,
                        request,
                        String.class);
//        this.logger.info("return value: " + responseEntity.toString());
        this.logger.info("return body value: " + responseEntity.getBody());

        // https://github.com/pravusid/springboot-vue.js-bbs/blob/master/src/main/java/kr/pravusid/service/JwtUserService.java

        Map token = JsonParserFactory.create().parseMap(responseEntity.getBody());
        returnString = (String) token.get("access_token");
//
//        } catch (RestClientException re) {
//            re.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return returnString;
    }
}

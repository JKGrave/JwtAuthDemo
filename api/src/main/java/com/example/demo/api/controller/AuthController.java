package com.example.demo.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private RestTemplate authRestTemplate;

    /* Resource config: jwt token converter will translate jwt token from header
     -> plain auth text shows -> filter chain process -> filter chain will give principal
    */
    @RequestMapping("/me")
    public String getPrincipal(Principal principal) {

        this.logger.info("principal returns: " + principal.getName());

        return principal.getName();
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public String requestJwtToken(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("clientId") String clientId,
            @RequestParam("clientSecret") String clientSecret,
            @RequestParam("grantType") String grantType,
            @RequestParam("scope") String scope,
            @RequestHeader(value = "Authorization", required = false) String authToken,
            Principal principal) {

        // already authorized
        if (authToken != null && !authToken.contains("undefined")
                && principal != null && !principal.getName().matches("anonymousUser")) {
            this.logger.info("already authenticated(name: " + principal.getName() + ")");
            return authToken.split(" ")[1]; // auth token expect format likes "Bearer {tokenValue}"
        }

        this.logger.info("request new auth token from jwtauth");
        // get jwt token
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", username);
        map.add("password", password);
        map.add("grant_type", grantType);
        map.add("scope", scope);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Basic Auth
        String plainCredentials = clientId + ":" + clientSecret;
        String base64ClientCredentials = new String(Base64Utils.encode(plainCredentials.getBytes()));
        httpHeaders.add("Authorization", "Basic " + base64ClientCredentials);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);

        this.logger.info("username: " + username + ", password: " + password
                + ", clientId: " + clientId
                + ", clientSecret: " + clientSecret
        );

        ResponseEntity<String> responseEntity = this.authRestTemplate
                .exchange(
                        "http://jwtauth/oauth/token",
                        HttpMethod.POST,
                        request,
                        String.class);
        this.logger.info("return body value: " + responseEntity.getBody());

        // https://github.com/pravusid/springboot-vue.js-bbs/blob/master/src/main/java/kr/pravusid/service/JwtUserService.java

        Map token = JsonParserFactory.create().parseMap(responseEntity.getBody());
        String returnString = (String) token.get("access_token");

        return returnString;
    }
}

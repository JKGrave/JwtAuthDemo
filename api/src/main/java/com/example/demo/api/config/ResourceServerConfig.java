package com.example.demo.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{

    private static final String signKey = "Sex";

    @Bean
    @Primary
    // Making this primary to avoid any accidental duplication with another token service instance of the same name
    // 액세스 토큰 및 새로 고침 토큰 값에 대해 임의의 UUID 값을 사용하는 토큰 서비스의 기본 구현체가 DefaultTokenServices.
    // TokenEnhancer 가 액세스 토큰과 리프레쉬 토큰을 생성 후 토큰이 tokenStore 에 저장되기 전에 커스터마이즈 하기 위함.
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        return defaultTokenServices;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signKey);
        converter.setVerifierKey(signKey);
        return converter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices());
    }

    /*
     * Input Type	: HttpSecurity
     * Output Type	: void
     * Description	: @EnableResourceServer를 통해 API서버가 OAuth2 인증을 받도록 한다.
     * 				  ResourceServerConfigurerAdapter를 상속받아 configure 메소드를 override하는데,
     * 				    해당 클래스는 OAuth2 security로 보호되는 경로에 대한 설정을 하는 역할을 한다.
     * 				    기본적으로는 모든 요청에 대해서 인증을 요구하나, /test 경로로 들어오는 요청에 대해서만 인증을 요구한다.
     * 				  scope가 read인 경우에만 해당 요청이 통과된다.
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable(); //HTTP 헤더 충돌 방지를 위해 설정
        http.authorizeRequests()
                .antMatchers("/test", "/test/**").access("#oauth2.hasScope('read')")
                .anyRequest().authenticated();
    }
}

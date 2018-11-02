package com.example.demo.jwtauth.config;

import com.example.demo.jwtauth.service.CustomClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

// https://stackoverflow.com/questions/49127791/extract-currently-logged-in-user-information-from-jwt-token-using-spring-securit

// basic spring security auth process(korean)
// https://tramyu.github.io/java/spring/spring-security/

// high class custom jwt auth
// https://sdqali.in/blog/2016/07/07/jwt-authentication-with-spring-web---part-4/
@Configuration
@EnableAuthorizationServer
public class JwtOAuthConfig extends AuthorizationServerConfigurerAdapter {

    private static final String signKey = "Sex";

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private AuthenticationManager authenticationManager; //인증을 담당하는 역할, 권한 등을 설정할 수 있는 객체

    @Autowired
    private CustomClientDetailsService customClientDetailsService;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signKey); // 서명 키 적용, MAC키 혹은 ssh-keygen으로 만든 RSA키
        return converter;
    }

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Input Type	: AuthorizationServerEndpointsConfigurer
     * Output Type	: void
     * Description	: OAuth2 서버가 작동하기 위한 endpoint에 대한 정보를 설정
     * 				    토큰 발급 및 저장에 대한 설정과 Spring Security와 OAuth를 연결한다.
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // 클라이언트가 사용할 새로운 토큰을 만드는 과정에서 액세스 토큰 (예 : 추가 정보 맵을 통해)을 사용자 정의 할 수 있도록 제공합니다.
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // accessTokenConverter() method에서 저장한 정보들(여기서는 sign key)  를 endPoints 에 적용
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter()));

        endpoints
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(this.authenticationManager)
                .tokenEnhancer(tokenEnhancerChain); // tokenServices()를 통해 커스터마이즈 적용 + TokenEnhancerChain 정보 적용
    }

    /*
     * Input Type	: ClientDetailsServiceConfigurer
     * Output Type	: void
     * Description	: API를 요청하는 클라이언트 정보를 설정한다.
     * 				  Client 서버(http://localhost:8083)로부터 클라이언트에 대한 정보를 가져와서 설정한다.
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(this.customClientDetailsService);
    }

    /*
     * Input Type	: AuthorizationServerSecurityConfigurer
     * Output Type	: void
     * Description	: Oauth 서버에서 발급한 access token을 다른 API 서버에서 사용할 때
     * 				    해당 토큰이 사용 가능한지를 체크할 수 있는 것을 허용
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception{
        oauthServer
                .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder());
    }

    /*
    * WebSecurityConfig의
    *   @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .addFilterBefore(ajaxAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        처럼 addFilterBefore작업을 통해 jwt전용 필터를 만들어야 한다.
        출처: http://heowc.tistory.com/46 [허원철의 개발 블로그]

        Claims를 통해 값을 추출하는 경우가 많다.
        spring security jwt 중 JwtHelper에 getClaims라는 메소드가 있는걸 보아 그것이 바로 뽑아내는 것 같다.
        이를 sign key를 이용해 뽑아내야 한다...
        그것을 통해 authorization 후 user information을 빼와야 됨.
    * */
}

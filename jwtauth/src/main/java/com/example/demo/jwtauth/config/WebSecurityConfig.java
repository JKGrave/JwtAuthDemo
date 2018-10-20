package com.example.demo.jwtauth.config;

import com.example.demo.jwtauth.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /*
     * Output Type	: AuthenticationManager
     * Description	: AuthenticationManager를 사용하기 위해 Bean으로 등록한다.
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /*
     * Input Type	: AuthenticationManagerBuilder
     * Output Type	: void
     * Description	: AuthenticationManagerBuilder를 통해 인증객체를 만들 수 있도록 한다.
     * 				  MyUserDetailService를 Autowired해주고, AuthenticationManagerBuilder에
     * 				    해당 빈을 주입
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.customUserDetailsService)
            .passwordEncoder(this.passwordEncoder);
    }

    /*
     * Input Type	: HttpSecurity
     * Output Type	: void
     * Description	: WebSecurityConfigurerAdapter를 상속받아 configure 메소드를 override하는데,
     * 				  security로 보호되는 경로에 대한 설정을 하는 역할을 한다.
     * 				    기본적으로는 모든 요청에 대해서 인증을 요구하나, 여기서는 모든 요청에 대해 permitAll을 적용하였음.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable(); //HTTP 헤더 충돌 방지를 위해 설정
        http.csrf().disable(); //
        http.authorizeRequests().antMatchers("/**").permitAll();
    }
}

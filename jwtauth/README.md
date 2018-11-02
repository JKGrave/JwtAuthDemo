JWT AUTH
=============

Compose
-------------

##### 1. spring
* spring security
  * spring oauth2
  * spring jwt
* cloud-netflix
  * eureka client 
* lombok (for domain)

##### 2. Authorization Server Config
[Intoduction of JWT] (https://jwt.io/introduction/)

1. token converter
    * jwt token으로 변환체를 생성한다(sign key를 입력)
    <pre><code>@Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signKey);
        return converter;
    }
    </code></pre>
   현재는 Verifier 를 여기에 두고 있지 않다.(api-gateway가 가지고 있다.) ~~아직 어떻게 할지 정책을 못정한거...~~ 
2. token store
    * token converter를 저장
    * jwt는 Token based authorization이기에 session 필요 없음. token 저장할 Db도 필요없음
    * 오로지 토큰만 들고 다니면서 인증을 하고 다닌다.
    <pre><code>@Bean
    public TokenStore tokenStore() {
       return new JwtTokenStore(accessTokenConverter());
    }
    </code></pre>

3. token service
    * 토큰이 발행되고 저장되기 전에 행동을 지정
    * 이 프로젝트에서는 tokenStore을 지정해주고, refresh 가능하게 만듦
    <pre><code>@Bean
    public JwtAccessTokenConverter accessTokenConverter() {
       JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
       converter.setSigningKey(signKey);
       converter.setVerifierKey(signKey);
       return converter;
    }</code></pre>
    
4. authorization endpoint config
    * OAuth2 서버가 작동하기 위한 endpoint에 대한 정보를 설정
    * 토큰 발급 및 저장에 대한 설정과 Spring Security와 OAuth를 연결한다.
    <pre><code>@Override
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
    }</code></pre>

5. Client details 정보 설정 <br/>
    여기서는 Client module에게서 불러올 것이다.
    <pre><code>@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
       clients.withClientDetails(this.customClientDetailsService);
    }
    </code></pre>
    
6. Authorization Server Security Config <br/>
    Oauth 서버에서 발급한 access token을 다른 API 서버에서 사용할 때 해당 토큰이 사용 가능한지를 체크할 수 있는 것을 허용<br/>
    비밀번호 인코딩 해놨기에 인코더 또한 설정
    <pre><code>@Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception{
       oauthServer
               .checkTokenAccess("isAuthenticated()")
               .tokenKeyAccess("isAuthenticated()")
               .passwordEncoder(passwordEncoder());
    }
    </code></pre>

7. Load Balanced RestTemplate 를 사용하여 api의 주소를 알지 않아도 된다.(Zuul, Erueka 필요)
 [참조 사이트] (http://2dell.blogspot.com/2018/06/spring-cloud-eureka.html)
     <pre><code>@LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
       return new RestTemplate();
    }</code></pre>
    
##### 3. Web Security Config
1.5X 버전대로 작성된 OAuth2(jwt 없음) 프로젝트다. 가져가서 맘대로 공부해라고 했음. [하드캐리 해준 형 땡큐!] (https://github.com/khyw407/oauth-example)

1. AuthenticationManager 를 사용하기 위해 Bean으로 등록한다. 인증업무를 수행할 것이다.

    <pre><code>
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    </code></pre>

2. AuthenticationManagerBuilder를 통해 인증객체를 만들 수 있도록 한다. <br/>
   MyUserDetailService를 Autowired해주고, AuthenticationManagerBuilder에 해당 빈을 주입

<pre><code>@Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this.customUserDetailsService)
            .passwordEncoder(this.passwordEncoder);
    }
</code></pre>

3. WebSecurityConfigurerAdapter를 상속받아 configure 메소드를 override하는데,
   security로 보호되는 경로에 대한 설정을 하는 역할을 한다. <br/>
   기본적으로는 모든 요청에 대해서 인증을 요구하나, 여기서는 모든 요청에 대해 permitAll을 적용하였음.
<pre><code>@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable(); //HTTP 헤더 충돌 방지를 위해 설정
        http.csrf().disable(); 
        http.authorizeRequests().antMatchers("/**").permitAll();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // JWT는 기본적으로 Session을 사용하지 않는다.
    }
</code></pre>
    
##### 4. JwtAuth 자체 Config
아직 없음...

 
##### 5. Domain
1. Client: Client의 정보를 담아 Client Module에 왔다갔다 할 객체.
2. Account: Spring Security의 UserDetails를 상속받아 구현. 유저 정보가 User Module에 왔다갔다 할 객체

##### 6. Service (구현 안빡시게 했음)
1. CustomClientDetailsService: Client Module 에게 RestTemplate 를 이용해 정보를 요청함. Zuul, Eureka 적용됨
2. CustomUserDetailsService: User Module 에게 RestTemplate 를 이용해 정보를 요청함. Zuul, Eureka 적용됨

##### 7. JwtAuthApplication.java
 [Read with] (https://codeburst.io/jwt-to-authenticate-servers-apis-c6e179aa8c4e)
 <pre><code>@SpringBootApplication
@EnableDiscoveryClient
public class JwtauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtauthApplication.class, args);
    }
}</code></pre>  
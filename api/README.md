API GATEWAY
=============

Compose
-------------

##### 1. spring
* spring security
  * spring oauth2
  * spring jwt
* cloud-netflix
  * eureka client 
  * zuul proxy

##### 2. Resource Config
[Intoduction of JWT] (https://jwt.io/introduction/)

1. token converter
    * jwt token으로 변환체를 생성한다(sign key, verify key를 입력)
    <pre><code>@Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signKey);
        converter.setVerifierKey(signKey);
        return converter;
    }
    </code></pre>
    
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
    
4. resource config
    * resource config에서 tokenService를 지정
    * 이제 이 Resource는 jwt token base로 인/디코딩을 합니다. ~~얘 마음대로 할 수 있는 겁니다.~~
    <pre><code>@Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices());
    }</code></pre>
    
##### 3. Http security Config

1. HTTP 헤더 충돌 방지를 위해 설정
> http.headers().frameOptions().disable();

2. antMatchers를 통해 접근권한 설정 <br/>
/test로 들어오는 모든 요청과 /auth로 들어오는 모든 요청은 oauth의 'read' scope가 있어야 접근 가능 <br/>
이 외의 요청은 인증만 되면 접근 됨 

<pre> <code>http.authorizeRequests()
                    .antMatchers("/test", "/test/**", "/auth/**").access("#oauth2.hasScope('read')")
                    //.antMatchers("/auth/request").permitAll() // 이 부분은 나중에 설정 및 설명, zuul 때문에...
                    .anyRequest().authenticated();
</code></pre>
3. JWT는 기본적으로 세션을 사용하지 않는다.
<pre> <code>http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);</code></pre>

##### 4. Api 자체 Config

1. Load Balanced RestTemplate 를 사용하여 api의 주소를 알지 않아도 된다.(Zuul, Erueka 필요)
 [참조 사이트] (http://2dell.blogspot.com/2018/06/spring-cloud-eureka.html)
 <pre><code>@LoadBalanced
@Bean
public RestTemplate restTemplate() {
   return new RestTemplate();
}
 </code></pre>
 
 ##### 5. Controller
 1. for Test (ApiController) : jwt 토큰을 헤더에 붙여서 들어가면 된다. 끝
 > filter chain들에 의해서 JWT가 verifier key에 의해 oauth2 token 형태로 decode되고 이를 알아서 읽을 것임
 
 2. Auth Controller(Zuul과 Eureka 인증 문제 해결하면 지워질 Controller)
 > jwtauth 모듈에게서 Token을 요청해온다.
 
 ##### 6. ApiApplication.java
 [Read with] (https://github.com/KD4/TIL/blob/master/MSA/zuul.md)
 <pre><code>
 @SpringBootApplication
 /*
  Spring Cloud Eureka는 넷플릭스에서 만든 서비스로,
  서버 들을 중앙 레지스트리에 등록하고 장애가 발생할 때 자동으로 제외, 새로운 서비스가 발견되면 자동으로 실시간으로 반영하는 도구이다.
  Eureka Server 와 Eureka Client 구조를 통해 Service Registry & Service Discovery 를 가능케 한다.
 */
 @EnableDiscoveryClient // eureka 에서 관리당함
 /*
 Zuul은 넷플릭스에서 만든 API Gateway로 그루비로 작성되었으며 filter 개념을 통해서 API Gateway에 추가 기능을 넣고 관리할 수 있다.
 
 Zuul은 Filter를 통해서 다음과 같은 기능을 구현하고있다.
 
 Authentication and Security : 클라이언트 요청시, 각 리소스에 대한 인증 요구 사항을 식별하고 이를 만족시키지 않는 요청은 거부
 Insights and Monitoring : 의미있는 데이터 및 통계 제공
 Dynamic Routing : 필요에 따라 요청을 다른 클러스터로 동적으로 라우팅
 Stress Testing : 성능 측정을 위해 점차적으로 클러스터 트래픽을 증가
 Load Shedding : 각 유형의 요청에 대해 용량을 할당하고, 초과하는 요청은 제한
 Static Response handling : 클러스터에서 오는 응답을 대신하여 API GATEWAY에서 응답 처리
 */
 @EnableZuulProxy // router, load balancer
 public class ApiApplication {
 
     public static void main(String[] args) {
         SpringApplication.run(ApiApplication.class, args);
     }
 }
 </code></pre>  
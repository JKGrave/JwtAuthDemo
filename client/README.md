CLIENT
---

Compose
-------------

##### 1. spring
* spring security
  * spring oauth2
  * spring jwt
* cloud-netflix
  * eureka client
* mysql db
  * jdbc
  * mysql connector
  * log4jdbc-log4j2-jdbc4.1 version 1.16(DB 자잘한 로그 다 띄워줌. 이 버전이 7 이상에서 동작함)
* lombok (for domain)

##### 2. controller
1. ClientController<br/>
    client 정보를 수집

##### 3. Client Domain
<pre><code>@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Client {

   @GeneratedValue(strategy = GenerationType.AUTO)
   private long seq;

   @Id
   private String clientId;

   private String clientSecret;

   private String scope;

   private String resourceIds;

   private String authorizedGrantTypes;

   private String registeredRedirectUris;

   private String autoApproveScopes;

   private String authorities;

   private Integer accessTokenValiditySeconds;

   private Integer refreshTokenValiditySeconds;
}</code></pre>

  DB에 객체화하여 집어넣어야하 하기 때문에 @Entity 추가

##### 4. repository
[Read with] (https://brunch.co.kr/@sbcoba/3)
> /{repository}/{id}/{property}, method=GET
/{repository}/{id}/{property}/{propertyId}, method=GET
/{repository}/{id}/{property}, method=DELETE
/{repository}/{id}/{property}, method=PATCH||POST||PUT
...

등등등 다양한 옵션을 @RepositoryRestResource 하나 달면 알아서 만듦 (나중에 서비스가 불어나면 그 때 적극적으로 사용할 예정)

##### 5. service
ClientRespository 를 통해 service 구현

##### 6. ClientApplication.java
데모 데이터를 CommandLineRunner를 통해 넣었음 <br/>
이 모듈도 Eureka가 감시하는 대상

<pre><code>@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableDiscoveryClient
public class ClientApplication implements CommandLineRunner {

   public static void main(String[] args) {
       SpringApplication.run(ClientApplication.class, args);
   }

   @Autowired
   private ClientRepository clientRepository;

   @Override
   public void run(String... args) {
       Client c1 = new Client();
       c1.setAccessTokenValiditySeconds(3000);
       c1.setRefreshTokenValiditySeconds(3000);
       c1.setAuthorities("CLIENT,ROLE_CLIENT");
       c1.setClientId("foo");
       c1.setClientSecret(new BCryptPasswordEncoder().encode("bar"));
       c1.setScope("read");
       c1.setAuthorizedGrantTypes("client_credentials,password,authorization_code");
       c1.setRegisteredRedirectUris("");
       this.clientRepository.save(c1);


       Client c2 = new Client();
       c2.setAccessTokenValiditySeconds(3000);
       c2.setRefreshTokenValiditySeconds(3000);
       c2.setAuthorities("CLIENT,ROLE_CLIENT");
       c2.setClientId("api");
       c2.setClientSecret(new BCryptPasswordEncoder().encode("apipassword"));
       c2.setScope("read");
       c2.setAuthorizedGrantTypes("client_credentials,password,authorization_code");
       c2.setRegisteredRedirectUris("");
       this.clientRepository.save(c2);


       Client c3 = new Client();
       c3.setAccessTokenValiditySeconds(24*60*60);
       c3.setRefreshTokenValiditySeconds(24*60*60);
       c3.setAuthorities("SYSTEM,ROLE_SYSTEM,CLIENT");
       c3.setClientId("eurekaserver");
       c3.setClientSecret(new BCryptPasswordEncoder().encode("eurekaserver"));
       c3.setScope("read");
       c3.setAuthorizedGrantTypes("client_credentials,password,authorization_code");
       c3.setRegisteredRedirectUris("");
       this.clientRepository.save(c3);
   }
}</code></pre>
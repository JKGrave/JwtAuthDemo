User
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
1. UserController<br/>
    Account 정보를 수집

##### 3. Account Domain
<pre><code>@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {

   @GeneratedValue(strategy=GenerationType.AUTO)
   private long seq;

   @Id
   @Column(length = 20)
   private String accountName;

   @Column(length = 100)
   private String accountPass;

   public Account(String accountName, String accountPass) {
       this.accountName = accountName;
       this.accountPass = accountPass;
   }
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
AccountRespository 를 통해 service 구현

##### 6. ClientApplication.java
데모 데이터를 CommandLineRunner를 통해 넣었음 <br/>
이 모듈도 Eureka가 감시하는 대상(데모 데이터 중 Eureka 정보는 아직은 필요 없습니다~)

<pre><code>@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableDiscoveryClient
public class UserApplication implements CommandLineRunner {

   public static void main(String[] args) {
       SpringApplication.run(UserApplication.class, args);
   }

   @Autowired
   private AccountRepository accountRepository;

   @Override
   public void run(String... args) throws Exception {
       Account a = new Account();
       a.setAccountName("user");
       a.setAccountPass(new BCryptPasswordEncoder().encode("password"));
       this.accountRepository.save(a);


       Account eurekaUser = new Account();
       eurekaUser.setAccountName("eurekaserver");
       eurekaUser.setAccountPass(new BCryptPasswordEncoder().encode("eurekaserver"));
       this.accountRepository.save(eurekaUser);
   }
}</code></pre>
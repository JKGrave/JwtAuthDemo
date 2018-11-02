EUREKA SERVER
=============

Compose
-------------

##### 1. spring
* spring security (안쓸 거 같기도...)
* cloud-netflix
  * eureka server
* configuration processor (필요값 자체 설정을 위해(will be deprecated))

##### 2. EurekaserverApplication.java
SecurityAutoConfiguration 을 off 했다.(2 버전대 이상에서는 더이상 application 외부 config 파일에서 security의 ON/OFF 제어가 안된다.)
<pre><code>@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableEurekaServer
public class EurekaserverApplication {

   public static void main(String[] args) {
       SpringApplication.run(EurekaserverApplication.class, args);
   }
}
</code></pre>

어노테이션 하나 달랑 달아주면 끝! 참 쉽죠?
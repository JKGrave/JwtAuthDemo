package com.example.demo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

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

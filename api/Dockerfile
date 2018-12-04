# ADD gs-spring-boot-docker-0.0.1-SNAPSHOT.jar api_gateway.jar
# RUN sh -c 'touch /api_gateway.jar'
# ENV JAVA_OPTS=""
# ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /api_gateway.jar"]

# https://spring.io/guides/gs/spring-boot-docker/

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD  build/libs/api-0.0.1-SNAPSHOT.jar apigateway.jar
EXPOSE 8081
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/apigateway.jar"]


# https://jinwooe.wordpress.com/2017/06/08/spring-boot-with-docker/
# FROM: jdk 를 컨테이너에 포함합니다.
# VOLUME: embedded tomcat 이 사용할 temp 디렉토리 입니다.
# ADD: artifact 이름을 버전과 함께 명시하고 app.jar 라는 이름으로 컨테이너에 포함합니다.
# RUN: app.jar 에 touch 명령을 실행시켜서 파일의 수정날짜를 변경합니다. 여기서는 큰 의미가 없지만, 파일의 수정 날짜가 요구되는 경우가 있습니다.
# ENV: Java의 실행 옵션을 지정합니다.
# ENTRYPOINT: Docker가 실행될 때 시작되는 명령입니다. 기본적으로  java -jar 과 다른 것이 없습니다. 시스템 환경 변수로 java.security.egd=file:/dev/./urandom 을 정의하고 있습니다. /dev/urandom 은 random number generator로 사용됩니다. tomcat의 startup 시간을 줄이는 효과가 있다고 합니다. 자세한 내용은 /dev/./urandom 을 참고하시기 바랍니다.
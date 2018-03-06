## 1. 스프링부트 관리자란?

Spring Boot Admin 은 애플리케이션을 HTTP 통신을 통해 관리하고 모니터링하는 서드파티 프로젝트입니다.



## 2. 시작하기

스프링부트 관리자는 server와 client 두 가지로 구분이됩니다. 예를 들자면 server는 모니터링 애플리케이션이 되고, client는 모니터링 하고자 하는 애플리케이션입니다.



### 2.1. 환경

| 항목         | 비고                     |
| ------------ | ------------------------ |
| JDK          | 1.8                      |
| SpringBoot   | 2.0.0.RELEASE            |
| Spring Stack | Reactive Stack (WebFlux) |



### 2.2. 스프링부트 어드민 설정

먼저 스프링부트 어드민을 설정해 보도록 하겠습니다. 필자는 현재 Spring5부터 지원되는 리액티브 프로그래밍인 Webflux를 사용하여 애플리케이션 개발해 보도록 하겠습니다.

#### 2.2.1. 의존성 추가

현재(2018.03.06)기준으로 스프링부트 어드민 2.0.0이 정식 릴리즈가 되지 않았기 때문에 아래와 같은 스냅샷 레파지토리를 등록해야 합니다.(정식 릴리즈가되면 해당 레파지토리는 제거해주시면 됩니다)

```grad
repositories {
    mavenCentral()
	maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
}

dependencies {
    compile('org.springframework.boot:spring-boot-starter-webflux')
	compile('de.codecentric:spring-boot-admin-starter-server:2.0.0-SNAPSHOT')
}
```

#### 2.2.2. 애노테이션 추가

스프링부트 어드민은 `@EnableAdminServer` 애노테이션 선언만으로 기본적인 설정을 자동으로 구성해줍니다.

```java
@Configuration
@EnableAutoConfiguration
@EnableAdminServer
public class SpringBootAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootAdminApplication.class, args);
    }
}
```

#### 2.2.3. 프로퍼티 설정

application.yml

```yml
server:
  port: 8081
```



### 2.3. 스프링부트 클라이언트 설정(등록)

스프링부트 어드민(SBA)에 모니터링할 애플리케이션을 등록하려면 스프링부트 클라이언트를 포함하시키너 Spring Cloud Discovery를 사용해야합니다. 필자는 간단하게 구성만을 할 것이기 때문에 스프링부트 클라이언트로 진행해 보도록 하겠습니다.



#### 2.3.1. 의존성 추가

스프링부트 클라이언트 또한 스냅샷 레파지토리를 등록해야 합니다. 그리고 애플리케이션의 보안을 위해서 `spring-boot-starter-security` 모듈도 추가해줍니다.

```gradle
repositories {
	mavenCentral()
	maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
}

dependencies {
	compile('org.springframework.boot:spring-boot-starter-security')
	compile('org.springframework.boot:spring-boot-starter-webflux')

	compile('de.codecentric:spring-boot-admin-starter-client:2.0.0-SNAPSHOT')
}
```



#### 2.3.2. 프로퍼티 설정

application.yml 파일을 생성하여 스프링부트 어드민 서버의 URL을 설정하여 스프링부트 어드민 클라이언트를 활성화 시켜줍니다.

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: ALWAYS

spring:
  application:
    name: springboot-admin-client
  boot:
    admin:
      client:
        url: http://localhost:8081 # 등록 할 Spring Boot Admin 서버의 URL.
server:
  port: 8082
```



#### 2.3.3. 시큐리티 설정

실제 애플리케이션에서는 보안된 종말점을 다루어야 하지만 해당 포스팅에서는 간결함을 위해 보안을 사용하지 않도록 하겠습니다.

```java
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange().anyExchange().permitAll()
                   .and().csrf().disable()
                   .build();
    }
}
```



## 3. 스크린샷

여기까지 보든 과정을 따라하셨다면 간단한 모니터링 애플리케이션이 구축되었습니다. 

![20180306_01_01](http://wonyoungpark.github.io/images/2018/0306_01_01.png)

![20180306_01_02](http://wonyoungpark.github.io/images/2018/0306_01_02.png)

![20180306_01_03](http://wonyoungpark.github.io/images/2018/0306_01_03.png)

![20180306_01_04](http://wonyoungpark.github.io/images/2018/0306_01_04.png)

![20180306_01_05](http://wonyoungpark.github.io/images/2018/0306_01_05.png)

## 4. 결론

[스프링부트 어드민 레퍼런스](http://codecentric.github.io/spring-boot-admin/current/#_what_is_spring_boot_admin)에서 보다 자세한 설명과 정보를 확인하실 수 있으며, 예제소스는 [제 github](https://github.com/WonYoungPark/springboot-admin-sample)에 올려놓았습니다. 해당 포스팅에서는 좀더 세분화되는 설정값등을 다루지 않지만 구글링을 통해서 충분이 정보를 검색하실 수 있을것입니다. 추후에 시간이 된다면 모니터링 값을 가지고 이메일 혹은 슬랙을 통해 노티를 발행하는 방법 또한 다루어 보도록 하겠습니다.



## 5. Reference

- [Spring Boot Admin 2.0.0.SNAPSHOT Reference Guide](http://codecentric.github.io/spring-boot-admin/current/#spring-cloud-discovery-static-config)
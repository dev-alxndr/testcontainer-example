# About TestContainer
## Home
### Testcontainer?
Testcontainer는 Java 라이브러리로 JUnit, 가볍게 쓰고 버릴 수 있는 일반적 데이터베이스, Selenium, Docker Container로 실행할 수 있는 모든 것을 지원합니다.

##### Testcontainer를 사용하면 좋은점
- Data Acess layer 통합 테스트
- Application 통합테스트
- UI 테스트

##### 사전 준비물
- Docker
- 지원하는 JVM 테스트 프레임워크
	- JUnit4
	- Jupiter/JUnit
	- Spock

##### Maven Dependencies
```gradle
testImplementation "org.testcontainers:testcontainers:1.17.3"
```
---
## Quick Start
### JUnit5
#### Redis Test
##### Without Testcontainer
```java
public class RedisBackedCacheIntTestStep0 {

    private RedisBackedCache underTest;

    @BeforeEach
    public void setUp() {
        // Assume that we have Redis running locally?
        underTest = new RedisBackedCache("localhost", 6379);
    }

    @Test
    public void testSimplePutAndGet() {
        underTest.put("test", "example");

        String retrieved = underTest.get("test");
        assertThat(retrieved).isEqualTo("example");
    }
}
```
위 테스트 코드는 모든 개발자와 CI 머신에 Redis가 설치되어 있다고 가정을 합니다. 설치가 되어있지 않다면 테스트는 실패하게 됩니다.

##### With Testcontainer
###### 의존성 추가
```gradle
testImplementation "org.junit.jupiter:junit-jupiter:5.8.1"
testImplementation "org.testcontainers:testcontainers:1.17.3"
testImplementation "org.testcontainers:junit-jupiter:1.17.3"
```
###### Redis Container 실행
```java
@Container
public GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
    .withExposedPorts(6379);
```
- `@Container` 어노테이션은 JUnit 테스트 수명 주기의 다양한 이벤트에 대해 이 필드를 알립니다.
- Docker Hub의 특정 Redis 이미지를 사용하도록 구성되고 포트를 노출하도록 구성됩니다

- 테스트 전 활성화
- Docker 설정을 확인
- 필요 시 이미지 받기
- 새로운 컨테이너 생성 후 준비를 기다림
- 테스트 후 컨테이너를 종료 후 삭제
###### Container와 통신 확인
```java
String address = redis.getHost();
Integer port = redis.getFirstMappedPort();

// Now we have an address and port for Redis, no matter where it is running
underTest = new RedisBackedCache(address, port);
```
> `redis.getHost()` 를 쓰세요. CI 환경이나 여러환경에서 다르게 동작할 수 있기 때문에 `localhost`를 쓰지말고 `redis.getHost()`를 쓰세요.
###### Run the Test
```java
@Testcontainers
public class RedisBackedCacheIntTest {

    private RedisBackedCache underTest;

    // container {
    @Container
    public GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:5.0.3-alpine"))
        .withExposedPorts(6379);

    // }

    @BeforeEach
    public void setUp() {
        String address = redis.getHost();
        Integer port = redis.getFirstMappedPort();

        // Now we have an address and port for Redis, no matter where it is running
        underTest = new RedisBackedCache(address, port);
    }

    @Test
    public void testSimplePutAndGet() {
        underTest.put("test", "example");

        String retrieved = underTest.get("test");
        assertThat(retrieved).isEqualTo("example");
    }
}
```

## References 
- [TestContainer Docs](https://www.testcontainers.org/)
- [Sigleton Container](https://www.testcontainers.org/test_framework_integration/manual_lifecycle_control)
- [TestContainer - Github](https://github.com/testcontainers/testcontainers-java/)
- [TestContainer 멱등성있는 환경 구축하기](https://medium.com/riiid-teamblog-kr/testcontainer-%EB%A1%9C-%EB%A9%B1%EB%93%B1%EC%84%B1%EC%9E%88%EB%8A%94-integration-test-%ED%99%98%EA%B2%BD-%EA%B5%AC%EC%B6%95%ED%95%98%EA%B8%B0-4a6287551a31)
- [어디서든 테스트 가능한 환경만들기 -redis](https://loosie.tistory.com/813)

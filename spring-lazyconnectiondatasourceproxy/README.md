# 🌐 LazyConnectionDataSourceProxy란?

`LazyConnectionDataSourceProxy`는 **Spring Framework**에서 제공하는 `javax.sql.DataSource`의 **Proxy 구현체**로, **실제 DB 커넥션을 지연해서 생성**하도록 도와줍니다.

즉, 트랜잭션 경계 내에서 실제 커넥션이 꼭 필요한 시점에만 커넥션을 확보합니다.

---

## ✅ 주요 목적

- **불필요한 커넥션 낭비 방지**

  → 트랜잭션이 시작되었더라도 쿼리가 실행되지 않으면 커넥션을 생성하지 않음.

- **트랜잭션 동기화된 커넥션 사용 보장**

  → `@Transactional` 같은 트랜잭션 경계 내에서 항상 **동일한 커넥션**을 사용하도록 보장.


---

## 🔧 사용 예시

```java
java
복사편집
@Bean
public DataSource dataSource() {
    // 실제 DB 커넥션을 제공하는 DataSource (ex. HikariDataSource)
    DataSource realDataSource = new HikariDataSource(...);

    // Proxy를 적용
    return new LazyConnectionDataSourceProxy(realDataSource);
}

```

---

## 🔄 동작 흐름

1. `@Transactional`이나 `TransactionManager`에 의해 트랜잭션이 시작됨
2. `LazyConnectionDataSourceProxy`는 **아직 실제 DB 커넥션을 요청하지 않음**
3. 실제 JDBC 작업(쿼리 실행 등)이 일어나는 시점에서 **Connection을 실제로 얻음**
4. 커넥션은 트랜잭션 범위 내에서 재사용됨

---

## 🔍 언제 유용한가?

| 상황 | 효과 |
| --- | --- |
| 트랜잭션 내에서 꼭 DB를 사용하지 않는 경우가 많은 로직 | 커넥션 자원 절약 |
| 멀티 DataSource 환경 (ex. RoutingDataSource와 함께 사용) | 실제 쿼리 시점에 커넥션 라우팅 가능 |
| 테스트 환경에서 `DataSource` 추적 필요 | 커넥션 획득 시점을 명확히 파악 가능 |

---

## ⚠️ 주의사항

- **트랜잭션 동기화가 없는 환경**에서는 기대하는 동작을 보장하지 않음
- 반드시 **트랜잭션 관리자가 커넥션 생명주기를 관리**하는 구조여야 효과적

---

## 📚 관련 클래스

- `org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy`
- `org.springframework.jdbc.datasource.DataSourceTransactionManager`
- `javax.sql.DataSource`
 
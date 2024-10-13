java로 개발을 하다보면 비동기적으로 처리를 해서 성능을 높여야 하는 경우가 생깁니다. 저 또한 이런 경우가 많았는데요, 이럴 때 Java에서 제공하는 CompletableFuture를 이용하면 쉽게 해결할 수 있습니다. CompletableFuture에 관하여 검색을 해보니, 박우빈님이 작성하신 [CompletableFuture 톺아보기](https://wbluke.tistory.com/50)  가 가장 정리가 잘 되어 있어서 한번 따라해보고, 해당 글을 기반으로 저 또한 정리를 해두고자 합니다!

### CompletableFuture란?

CompletableFuture는 Java에서 비동기 프로그래밍을 지원하는 강력한 클래스입니다. Java 8부터 도입되었으며, 사용하기에 따라서 Async-Blocking, Async-Non-Blocking 하게 사용할 수 있습니다.

CompletableFuture에서 제공하는 기능은 많지만, 이번 포스팅에서는 주요 기능들에 대해서만 테스트코드와 함께 다뤄보도록 하겠습니다.

이번 포스팅에서 작성한 예제코드는 [Github](https://github.com/DevCHW/tutorial-code) 에서 볼 수 있습니다. 

### 예제코드 작성 환경 구성

```
dependencies {
    // spring web
    implementation 'org.springframework.boot:spring-boot-starter-web'

    // lombok
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'

    // test lombok
    testCompileOnly 'org.projectlombok:lombok'
    testAnnotationProcessor 'org.projectlombok:lombok'

    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

예제 코드 작성을 위하여 build.gradle에 위와 같은 의존성들을 추가해줍니다.

간단하게 스프링과 롬복정도만 추가해주시면 됩니다.

그리고 예제코드에서 쭉 사용할 유틸 클래스를 테스트 쪽에 하나 만들어줍니다.

```
package com.study.completeablefuture;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PrintUtil {

    public static String print(String message) {
        sleep(1000);
        log.info(message);
        return message;
    }

    private void sleep(long millis) {
        try {
            log.info("start to sleep {} second.", millis);
            Thread.sleep(millis);
            log.info("end to sleep {} second.", millis);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

}
```

간단하게 1초를 쉰 다음, 로그로 메세지를 출력하는 유틸리티 함수입니다.

1초를 쉬는 타이밍을 정확히 알기 위하여 Thread.sleep 위아래로 로그를 출력하겠습니다.

### 예제 코드를 통해 CompletableFuture에 대해 알아보자

먼저 CompleteableFuture의 기본이 되는 메소드 3가지를 알아보겠습니다.

#### supplyAsync / runAsync / join

supplyAsync

```
@Slf4j
public class CompletableFutureBasicTest {

    @Test
    void supplyAsync() {
        // given
        String message = "hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

        // when
        String result = messageFuture.join();

        // then
        assertThat(result).isEqualTo(message);
    }

}
```

supplyAsync는 파라미터로 Supplier 인터페이스를 받아서 **반환값이 존재하는 메소드** 입니다.

PrintUtil.print() 메소드는 반환 타입이 String이기 때문에 CompletableFuture의 제네릭 타입에는 String을 명시해주면 됩니다.

위의 given절 처럼 supplyAsync 메소드를 통해 PrintUtil.print() 메소드를 콜백으로 넘겨주면 작업이 완료될 때 까지 기다리지 않고 비동기적으로 다음 로직을 수행할 수 있게 됩니다.

when 절의 join()은 CompletableFuture의 작업이 끝날 때 까지 기다리는 Blocking 메소드입니다. 따라서 given절에서 supplyAsync 메소드에 넘긴 PrintUtil.print()의 작업이 끝날 때 까지 기다렸다가, 결과값을 받을 수 있습니다.

runAsync

```
@Test
void runAsync() {
    // given
    String message = "hello";
    CompletableFuture<Void> messageFuture = CompletableFuture.runAsync(() -> PrintUtil.print(message));

    // when
    messageFuture.join();
}
```

runAsync는 supplyAsync와는 다르게, Runnable 인터페이스를 파라미터로 받아 콜백을 수행하기 때문에 반환값이 없는 메소드입니다.

따라서 CompletableFuture의 제네릭 타입에는 Void로 명시해줍니다.

정리하자면, 콜백으로 넘겨준 반환값이 필요할 때는 supplyAsync, 필요하지 않을 때는 runAsync 메소드를 활용하면 적절합니다. 

반환값이 없기 때문에 검증할 부분도 없어서 runAsync 테스트 작성은 이만 넘어가도록 하겠습니다.

join

위에서 설명한 것 처럼 join은 해당 CompletableFuture의 콜백이 수행될 때 까지 기다리는 Blocking 메소드입니다.

참고로 join 대신 get도 동일한 기능을 하지만, get을 사용하면 내부적으로 체크 예외를 던지기 때문에 처리를 해줘야 합니다.

보통 실무에서는 체크 예외를 대부분 사용하지 않기 때문에 get 대신 join을 사용하는 것이 대부분 적절할 것으로 보입니다.

#### completedFuture

```
@Test
void completedFuture() {
    // given
    String message = "Hello";
    CompletableFuture<String> messageFuture = CompletableFuture.completedFuture(message);

    // when
    String result = messageFuture.join();

    // then
    assertThat(result).isEqualTo("Hello");
}
```

completedFuture는 이미 완료된 작업이나, 정적인 값을 CompletableFuture로 감쌀 때 사용합니다.

비동기 작업이 필요하지 않은 상황에서도 CompletableFuture의 일관성을 유지하면서 결과를 다루고 싶을 때 유용할 것 같습니다.

#### thenApply / thenAccept

thenApply와 thenAccept는 메서드 체이닝 형식으로 CompletableFuture에 후속 작업을 지정할 수 있는 메소드입니다.

thenApply

```
@Test
void thenApply() {
    // given
    String message = "hello";
    CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

    // when
    String result = messageFuture
            .thenApply(printMessage -> printMessage + " world")
            .join();

    // then
    assertThat(result).isEqualTo("hello world");
}
```

thenAccept

```
@Test
void thenAccept() {
    // given
    String message = "hello";
    CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

    // when
    messageFuture
            .thenAccept(printMessage -> {
                String result = printMessage + " world";
                log.info("result ={}", result);
            })
            .join();
}
```

thenApply와 thenAccept는 supplyAsync와 runAsync처럼 반환값이 있는지 없는지 차이입니다.

위의 thenApply 테스트에서는 CompletableFuture에서 반환한 결과값 이후에 world를 더하여 hello world를 만들고, join을 통해 Blocking을 걸어 반환받은 뒤 검증하였고, thenAccept의 테스트에서는 반환값이 없기 때문에 로그 출력만 하였습니다.

### exceptionally / handle

CompletableFuture를 통해 비동기 처리를 할 때 예외에 대한 처리를 할 수 있는 exceptionally / handle 메소드에 대하여 알아보겠습니다.

먼저 위에서 작성해둔 PrintUtil에 예외를 발생시키는 메소드를 하나 추가해주도록 합니다.

```
// PrintUtil.java
public static String printException(String message) {
    sleep(1000);
    log.info(message);
    throw new RuntimeException("에러가 발생했습니다 ㅠㅠ");
}
```

exceptionally

```
@Test
void exceptionally() {
    // given
    String message = "hello";
    CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.printException(message));

    // when
    String result = messageFuture
            .exceptionally(exception -> {
                if (exception instanceof RuntimeException) {
                    return "런타임 에러가 났나봐요..";
                }
                return null;
            })
            .join();

    // then
    assertThat(result).isEqualTo("런타임 에러가 났나봐요..");
}
```

exceptionally에서는 발생한 예외를 인자로 받아서 처리를 할 수 있는데요, 위 테스트 처럼 if문으로 분기 처리를 할 수도 있고, 다른 동작을 하도록 할 수 있습니다.

예외가 발생하지 않았다면 exceptionally는 무시되고 처리가 됩니다. 간단하게 CompletableFuture의 콜백 메소드를 try-catch 구문처럼 처리할 수 있다고 보면 이해가 쉽습니다!

handle

```
@Test
void handle() {
    // given
    String message = "hello";
    CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> PrintUtil.print(message));

    // when
    String result = messageFuture
            .handle((printMessage, throwable) -> printMessage + "world")
            .join();

    // then
    assertThat(result).isEqualTo("hello world");
}
```

handle 메소드의 첫번째 인자로는 콜백 메서드의 정상 결과값을, 두번째 인자로는 예외를 받아 처리할 수 있습니다.

만약 콜백 메서드에서 예외가 발생하지 않았다면 두번째 인자는 null이 됩니다.

실제로 활용할 땐 두번째 인자의 null 체크를 통해 예외가 발생했는지 아닌지 처리할 수 있겠네요!

#### allOf

```
@Test
void allOf() {
    // given
    List<String> messages = Arrays.asList("apple", "banana", "water");

    List<CompletableFuture<String>> messageFutures = messages.stream()
            .map(message -> CompletableFuture.supplyAsync(() -> PrintUtil.print(message)))
            .toList();

    // when
    List<String> result = CompletableFuture.allOf(messageFutures.toArray(messageFutures.toArray(new CompletableFuture[0])))
            .thenApply(Void -> messageFutures.stream()
                    .map(CompletableFuture::join)
                    .toList())
            .join();

    // then
    assertThat(result).isEqualTo(messages);
}
```

allOf는 여러 비동기 작업을 한번에 Blocking을 걸 때 유용합니다.

위 코드를 설명하자면, 여러개의 메세지를 만들어 스트림 변환을 통해 CompletableFuture 리스트를 만들어준 뒤, when 절에서 allOf를 통해 Blocking을 걸어주었습니다.

allOf는 단순히 Blocking만 걸 뿐, 반환값이 없습니다. 만약 여러 비동기 처리의 결과값이 필요한 경우, 위에서 학습한 thenApply와 join을 이용하여 예제코드처럼 결과값을 반환하도록 할 수 있습니다.

### \~~~Async

CompletableFuture 메서드들을 IDE에서 타이핑하며 자동완성되는 부분을 자세히 보면 대부분 suffix로 Async가 붙어있는 메소드들이 하나씩 있는 것들을 볼 수 있습니다.

![##_Image|kage@boOOJs/btsJ4bW2tdg/WyKCqV8I9v1LsLbqwDMru1/img.png|CDM|1.3|{"originWidth":1391,"originHeight":439,"style":"alignCenter","filename":"2024-10-13 12 05 21.png"}_##](https://blog.kakaocdn.net/dn/boOOJs/btsJ4bW2tdg/WyKCqV8I9v1LsLbqwDMru1/img.png)

이처럼 뒤에 Async가 붙어있는 메서드들은 두번째 파라미터로 Executor(스레드 풀)을 넣어줄 수 있습니다.

스레드 풀을 파라미터로 같이 넘겨주게 되면, 해당 콜백메서드는 넘겨준 스레드 풀에서 스레드를 꺼내 수행하게 됩니다.

위의 예제코드들처럼 스레드 풀을 지정하지 않는다면 기본으로 선택된 스레드풀에서 수행합니다.  

![##_Image|kage@bxLs4Y/btsJ4bbGlfk/08PPQ4i47kNHFYDsFMx6Uk/img.png|CDM|1.3|{"originWidth":1937,"originHeight":1180,"style":"alignCenter","filename":"2024-10-13 12 17 53.png"}_##](https://blog.kakaocdn.net/dn/bxLs4Y/btsJ4bbGlfk/08PPQ4i47kNHFYDsFMx6Uk/img.png)

작성한 예제코드들 중 하나를 골라 실행해보면, 로그를 통해 Java에서 기본으로 제공하는 ForkJoinPool을 사용하여 실행된 것을 확인할 수 있습니다.

이러한 점을 활용하여 실무에서는 어플리케이션 내부에서 비동기로 처리될 로직들의 스레드 풀과 동기적으로 처리될 스레드 풀을 나누어 효율적으로 풀 사이즈를 지정할 수 있고, 비동기로 처리될 로직들 중에서도 데드락 위험 여지가 있는 부분들에 대하여 스레드 풀을 따로 지정하여 수행하도록 처리하여 데드락 방지를 하는 등 상황에 따라 효과적으로 처리할 수 있을 것 같습니다.

### 마치며..

여기까지 CompletableFuture에 대하여 주요한 메서드들에 대하여 살펴보았습니다. 비동기 처리에 대한 이해가 이전까지 와닿지 않았는데, 직접 실행하고 결과값을 확인해보며 비동기와 한층 더 친해진 느낌입니다.

학습을 하면서 이전에 작성했던 비효율적인 코드들이 떠올랐습니다. 시간적인 여유가 허락할 때 얼른 업무에도 직접 CompletableFuture를 적용하여 드라마틱한 성능 개선을 해보고 싶습니다.

### 참고 자료

[Java CompletableFuture로 비동기 적용하기](https://11st-tech.github.io/2024/01/04/completablefuture/#%EB%B9%84%EB%8F%99%EA%B8%B0-%EB%A9%94%EC%84%9C%EB%93%9C)

[CompletableFuture 톺아보기](https://wbluke.tistory.com/50)
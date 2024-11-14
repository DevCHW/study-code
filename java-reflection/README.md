안녕하세요 ! 오늘은 Java의 강력한 기능인 리플렉션에 대해서 알아보겠습니다.

예제에 쓰인 모든 코드는 [깃허브](https://github.com/DevCHW/study-code/tree/main/java-reflection) 에서 확인하실 수 있습니다.

### 리플렉션이란?

리플렉션이란, **런타임**에 클래스와 인터페이스 등을 검사하고 조작할 수 있는 기능입니다.

구체적으로 설명을 위해 먼저 JVM의 동작 흐름에 대한 그림을 보겠습니다.

![](https://blog.kakaocdn.net/dn/mG1LE/btsKI4cDJG1/GhnZqrv7yhf6Cs2QTKDS9k/img.png)

1\. Java 컴파일러(javac)가 소스코드를 바이트 코드(.class)로 변환합니다.

2\. 컴파일된 바이트 코드를 클래스 로더에게 전달합니다.

3\. 클래스 로더는 동적 로딩을 통해 필요한 클래스들을 링크하여 런타임 데이터 영역에 올립니다.

여기서 리플렉션은 Method Area의 클래스 정보들을 조작할 수 있는 기능입니다.

###  어떻게 Method Area에 접근하는가?

자바에서는 Method Area의 클래스 정보를 가져오기 위한 Class 클래스를 제공합니다.

Class 클래스 정보를 가져오기 위하여 아래 코드처럼 3가지 방법을 사용할 수 있습니다.

```
// com.devchw.javareflection.MyClass
class MyClass {
    private final String message = "Hello World";
}
    
public class GetClass {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        // (1) '클래스 타입'.class();
        final Class<MyClass> clazz1 = MyClass.class;

        // (2) '인스턴스'.getClass();
        MyClass myClass = new MyClass();
        final Class<? extends MyClass> clazz2 = myClass.getClass();

        // (3) Class.forName("풀 패키지 경로");
        Class<?> clazz3 = Class.forName("com.devchw.javareflection.MyClass");

        System.out.println("clazz1 = " + clazz1); // class com.devchw.javareflection.MyClass
        System.out.println("clazz2 = " + clazz2); // class com.devchw.javareflection.MyClass
        System.out.println("clazz3 = " + clazz3); // class com.devchw.javareflection.MyClass
    }
}
```

이렇게 3가지 방법을 통해서 Class 객체를 이용하면 원본 클래스의 메타데이터 정보를 가져올 수 있는데요, \`getXXX()\` 메소드는 클래스의 public 데이터만 가져올 수 있고,  \`getDeclaredXXX()\` 메소드는 접근 제한자에 상관없이 모든 메타데이터를 가져올 수 있습니다.

### 리플렉션 사용해보기

리플렉션을 통해서 다음과 같은 정보들을 가져올 수 있습니다.

1\. 필드

2\. 메서드

3\. 생성자

4\. Enum

5\. Annotation

6\. 배열

7\. 부모 클래스와 인터페이스

예제에서는 필드, 메서드, 생성자 정보를 가져오고, 값을 조작해보도록 하겠습니다.

**MyClass**

```
package com.devchw.javareflection;

class MyClass {
    private final String message = "Hello World";
    private String hello;

    public MyClass() {
    }

    public String getMessage() {
        return message;
    }

    public String getHello() {
        return hello;
    }

    private MyClass(String hello) {
        this.hello = hello;
    }

    private void print(String message) {
        System.out.println(message);
    }

}
```

```
package com.devchw.javareflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        reflectionField();
        reflectionMethod();
        reflectionConstructor();
    }

    /**
     * 리플렉션으로 private final 필드 값 조작하기
     */
    private static void reflectionField() throws NoSuchFieldException, IllegalAccessException {
        // 클래스 정보 가져오기
        final Class<MyClass> clazz1 = MyClass.class;
        Field declaredField = clazz1.getDeclaredField("message");
        declaredField.setAccessible(true); // 필드에 접근할 수 있도록 지정

        // 인스턴스 생성
        MyClass myClass = new MyClass();

        // message 필드 출력
        String message1 = (String) declaredField.get(myClass);
        System.out.println("message = " + message1); // Hello World

        // message 필드 값 조작
        declaredField.set(myClass, "Bye World");
        String message2 = (String) declaredField.get(myClass);
        System.out.println("message = " + message2); // Bye World
    }

    /**
     * 리플렉션으로 private 메소드 호출하기
     */
    private static void reflectionMethod() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Class<MyClass> clazz = MyClass.class;
        Method declaredMethod = clazz.getDeclaredMethod("print", String.class);
        declaredMethod.setAccessible(true);

        MyClass myClass = new MyClass();
        declaredMethod.invoke(myClass, "Hello World"); // Hello World
    }

    /**
     * 리플렉션으로 private 생성자 호출하기
     */
    private static void reflectionConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final Class<MyClass> clazz = MyClass.class;
        Constructor<MyClass> constructor = clazz.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        MyClass myClass = constructor.newInstance("헬로");
        System.out.println(myClass.getHello()); // 헬로
    }

}
```

여기서 주의할 점은 .setAccessible(true)를 하지 않는다면 private에 접근할 수 없기 때문에 IllegalAccessException이 발생하므로,  accessible를 true로 지정해주셔야 합니다. 해야만 값을 조작할 수 있습니다.

### 리플렉션 활용

이렇게 리플렉션을 이용하면 원래 접근하지 못할 필드나 메서드, 생성자 등등에 대해서 런타임 시점에 조작을 할 수 있게됩니다.

이런 편리한 리플렉션은 어디에서 활용이 될까요?

-   동적으로 Class를 사용해야 하는 경우  
    코드 작성 시점에서는 어떠한 Class를 사용해야할 지 모르지만 Runtime에 Class 정보를 가져와서 실행해야 하는 경우(Spring Annotation) 등
-   Test Code 작성  
    테스트 코드 작성 시 private로 닫혀있는 생성자나 필드를 조작하여 객체를 생성하고 싶을 때 또는 private method를 테스트하고 싶은 경우
-   Jackson, GSON 등의 JSON Serialization Library  
    JSON 데이터를 클래스로 바인딩할 때 private로 필드가 닫혀있어도 Reflection을 이용하여 값을 바인딩해줄 수 있습니다. 
-   정적 분석 tool

### 주의사항

리플렉션을 이용하면 마법처럼 접근하지 못하는 메타데이터에 대해서 조작할 수 있기 때문에 편리하지만, 이를 남발해서는 안됩니다.

1\. 성능저하

\- 리플렉션은 런타임 시점에 추가적인 처리를 하기 때문에, 성능 저하가 있을 수 있습니다.

2\. 안정성

\- private같이 의도적으로 외부에서의 접근을 막아두었지만, 리플렉션을 이용하여 값을 조작해버리면 개발자의 의도와 다르게 동작할 수 있기 때문에 예상치 못한 에러가 발생할 수 있습니다.

3\. 보안

\- 리플렉션을 통해 비공개 멤버에 접근할 수 있기 때문에, 보안 취약점이 될 수 있습니다.

4\. 유지보수성 저하

리플렉션을 사용한 코드는 직관적이지 않기 때문에 코드 가독성이 좋지 못하고, 문제가 발생했을 때 이를 추적하기 어렵습니다.

따라서 리플렉션을 사용하는 것은 최대한 **지양** 하고 다른 대안을 먼저 모색한 다음 꼭 필요할 때 사용하는 것이 좋습니다.

### 참고자료

[\[Java\] Reflection은 무엇이고 언제/어디서 사용하는 것이 좋을까?](https://velog.io/@alsgus92/Java-Reflection%EC%9D%80-%EB%AC%B4%EC%97%87%EC%9D%B4%EA%B3%A0-%EC%96%B8%EC%A0%9C%EC%96%B4%EB%96%BB%EA%B2%8C-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94-%EA%B2%83%EC%9D%B4-%EC%A2%8B%EC%9D%84%EA%B9%8C)

[\[10분 테코톡\] 헙크의 자바 Reflection](https://www.youtube.com/watch?v=RZB7_6sAtC4)
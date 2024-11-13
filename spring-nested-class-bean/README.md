### Inner Class를 Bean으로 등록할 수 없는 경우

```
public class OuterClass {

    @Component
    class InnerClass {
    }

}
```

스프링 프로젝트 내부에 위와 같이 선언한 이너 클래스가 있습니다. 이너 클래스에 @Component를 붙여서 Bean으로 등록하려 하면 과연 Bean으로 등록이 잘 될까요?

```
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringNestedClassBeanApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(SpringNestedClassBeanApplication.class, args);
        OuterClass.InnerClass innerClass = ac.getBean(OuterClass.InnerClass.class);
        System.out.println("innerClass = " + innerClass);
    }

}
```

확인을 위해 ApplicationContext에서 getBean() 메서드를 통해 InnerClass의 Bean을 가져와서 print하도록 해보았습니다.

![](https://blog.kakaocdn.net/dn/rdotJ/btsKFobGLzd/nv4Zp5BQF0NWhI3JbGfSaK/img.png)

코드를 실행해보면, NoSuchBeanDefinitionException이 발생하며 InnerClass Bean을 찾을 수 없다고 합니다.

### Inner Class를 Bean으로 등록할 수 있는 경우

```
import org.springframework.stereotype.Component;

@Component // 추가
public class OuterClass {

    @Component
    class InnerClass {
    }

}
```

이번에는 OuterClass에도 @Component를 달아서 OuterClass, InnerClass 둘 다 Bean으로 등록한 뒤 코드를 실행해보겠습니다.

![](https://blog.kakaocdn.net/dn/lU5Xc/btsKHO0yCSC/n1HrJREQBdBH98l2vSqAx1/img.png)

이번에는 Bean이 조회가 되어 정상적으로 출력이 된 모습을 확인할 수 있습니다.

### 왜 OuterClass를 Bean으로 등록하지 않으면 InnerClass를 Bean으로 등록할 수 없을까?

기본적으로 InnerClass는 **OuterClass의 인스턴스가 없이는 객체 생성이 불가능**합니다.

![](https://blog.kakaocdn.net/dn/5JFoo/btsKFH9UyLk/LX8Ol8EkdNa642kNWJoWfk/img.png)

따라서 스프링 컨테이너 초기화 시점에 컴포넌트 스캔 대상의 클래스들을 읽어 Bean으로 등록할 클래스들은 인스턴스를 생성한 뒤 컨텍스트에 등록하여 싱글톤으로 관리해야 하지만, **OuterClass의 인스턴스가 없으면 InnerClass의 인스턴스를 생성할 수 없기 때문에 Bean으로 등록할 수 없습니다.**

이러한 이유로, OuterClass를 Bean으로 등록하면, OuterClass의 인스턴스를 통해 InnerClass의 인스턴스도 생성할 수 있기 때문에

이런 경우에는 InnerClass를 Bean으로 등록할 수 있게 됩니다.

또한 중첩 클래스를 static으로 선언하면 OuterClass 없이 인스턴스를 생성할 수 있기 때문에 이 경우에도 중첩 클래스를 Bean으로 등록할 수 있게 됩니다.

참고로 중첩 클래스(Static Nested Class)와 이너 클래스(Non-Static Nested Class)는 다른 개념이긴 합니다.

```
import org.springframework.stereotype.Component;

public class OuterClass {

    @Component
    static class StaticNestedClass {
    }

}


@SpringBootApplication
public class SpringNestedClassBeanApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(SpringNestedClassBeanApplication.class, args);
        OuterClass.StaticNestedClass staticNestedClass = ac.getBean(OuterClass.StaticNestedClass.class);
        System.out.println("staticNestedClass = " + staticNestedClass);
        // staticNestedClass = com.devchw.springnestedclassbean.OuterClass$StaticNestedClass@5922d3e9
    }

}
```

물론 개발을 하면서 InnerClass를 Bean으로 등록해서 사용할 일은 거의 없습니다만..(한번도 본 적 없음) 다시한번 스프링의 핵심 원리에 대해서 생각하게 되어 개인적으로 좋은 주제라고 생각이 되어 글로 정리해보게 되었습니다.

예제에 쓰인 모든 코드는 [깃허브](https://github.com/DevCHW/study-code/tree/main/spring-nested-class-bean) 에서 확인하실 수 있습니다.

감사합니다. 

### **참고자료**

[자바의 내부 클래스는 스프링 빈이 될 수 있을까?](https://www.youtube.com/watch?v=2G41JMLh05U)
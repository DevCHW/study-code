package com.devchw.springnestedclassbean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringNestedClassBeanApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(SpringNestedClassBeanApplication.class, args);

        // Bean으로 등록할 수 없는 경우
        OuterClass1.InnerClass innerClass1 = ac.getBean(OuterClass1.InnerClass.class);
        System.out.println("innerClass1 = " + innerClass1); // NoSuchBeanDefinitionException

        // Bean으로 등록할 수 있는 경우
        OuterClass2.InnerClass innerClass2 = ac.getBean(OuterClass2.InnerClass.class);
        System.out.println("innerClass2 = " + innerClass2); // innerClass2 = com.devchw.springnestedclassbean.OuterClass2$InnerClass@47406941

        // 정적 중첩 클래스는 외부 클래스 없이 인스턴스 생성이 가능하기 때문에, Bean으로 등록 가능
        OuterClass3.StaticNestedClass staticNestedClass = ac.getBean(OuterClass3.StaticNestedClass.class);
        System.out.println("staticNestedClass = " + staticNestedClass); // staticNestedClass = com.devchw.springnestedclassbean.OuterClass$StaticNestedClass@5922d3e9

    }

    public void createInnerClassInstance() {
//        new OuterClass1.InnerClass(); // 인스턴스 생성 불가능

        new OuterClass1().new InnerClass(); // 인스턴스 생성 가능
        new OuterClass3.StaticNestedClass(); // 인스턴스 생성 가능
    }

}

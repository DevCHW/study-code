package com.devchw.springnestedclassbean;

import org.springframework.stereotype.Component;

@Component
public class OuterClass2 {

    @Component
    class InnerClass {

    }
}

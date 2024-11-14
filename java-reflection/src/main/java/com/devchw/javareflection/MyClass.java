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

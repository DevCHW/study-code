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

package com.devchw.javareflection;

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

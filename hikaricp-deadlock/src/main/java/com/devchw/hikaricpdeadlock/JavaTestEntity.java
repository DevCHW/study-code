package com.devchw.hikaricpdeadlock;

import jakarta.persistence.*;

@Entity
@Table(name = "java_test_entity")
public class JavaTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public JavaTestEntity(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

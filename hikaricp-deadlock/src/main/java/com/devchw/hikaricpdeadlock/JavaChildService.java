package com.devchw.hikaricpdeadlock;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JavaChildService {

    private final JavaTestEntityJpaRepository javaTestEntityJpaRepository;

    public JavaChildService(JavaTestEntityJpaRepository javaTestEntityJpaRepository) {
        this.javaTestEntityJpaRepository = javaTestEntityJpaRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveTestEntity() {
        javaTestEntityJpaRepository.save(new JavaTestEntity("헬로"));
    }
}

package com.devchw.hikaricpdeadlock;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JavaParentService {

    private final JavaChildService javaChildService;

    public JavaParentService(JavaChildService javaChildService) {
        this.javaChildService = javaChildService;
    }

    @Transactional
    public void saveTestEntity() {
        javaChildService.saveTestEntity();
    }
}

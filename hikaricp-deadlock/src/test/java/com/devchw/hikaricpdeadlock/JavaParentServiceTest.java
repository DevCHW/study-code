package com.devchw.hikaricpdeadlock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JavaParentServiceTest {

    @Autowired
    private JavaParentService javaParentService;

    @Test
    public void HikariCP_DeadLock_발생_테스트() {
        javaParentService.saveTestEntity();
    }
}
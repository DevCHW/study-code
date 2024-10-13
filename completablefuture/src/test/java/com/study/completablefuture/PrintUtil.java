package com.study.completablefuture;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PrintUtil {

    public static String print(String message) {
        sleep(1000);
        log.info(message);
        return message;
    }

    private void sleep(long millis) {
        try {
            log.info("start to sleep {} second.", millis);
            Thread.sleep(millis);
            log.info("end to sleep {} second.", millis);
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }

    public static String printException(String message) {
        sleep(1000);
        log.info(message);
        throw new RuntimeException("에러가 발생했습니다 ㅠㅠ");
    }

}

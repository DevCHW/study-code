package com.study.lazyconnectiondatasourceproxy.domain;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityJpaRepository userEntityJpaRepository;
    private final HikariDataSource hikariDataSource;

    @Transactional(transactionManager = "defaultDataSourceTransactionManager")
    public void createUser1() {
        final HikariPoolMXBean hikariPoolMXBean = hikariDataSource.getHikariPoolMXBean();
        log.info("save() 호출 전 활성 커넥션 수 : {}", hikariPoolMXBean.getActiveConnections());
        
        // 실제로 커넥션이 필요한 시점
        userEntityJpaRepository.save(new UserEntity("Hyunwoo Choi", 29));
        log.info("save() 호출 이후 활성 커넥션 수 : {}", hikariPoolMXBean.getActiveConnections());
    }

    @Transactional(transactionManager = "lazyConnectionDataSourceProxyTransactionManager")
    public void createUser2() {
        final HikariPoolMXBean hikariPoolMXBean = hikariDataSource.getHikariPoolMXBean();
        log.info("save() 호출 전 활성 커넥션 수 : {}", hikariPoolMXBean.getActiveConnections());

        // 실제로 커넥션이 필요한 시점
        userEntityJpaRepository.save(new UserEntity("Hyunwoo Choi", 29));
        log.info("save() 호출 이후 활성 커넥션 수 : {}", hikariPoolMXBean.getActiveConnections());
    }

}

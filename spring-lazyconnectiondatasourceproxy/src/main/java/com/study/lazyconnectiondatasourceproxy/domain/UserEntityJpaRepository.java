package com.study.lazyconnectiondatasourceproxy.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserEntityJpaRepository extends JpaRepository<UserEntity, Long> {
}

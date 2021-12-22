package com.niemczuk.passwordwallet.repository;

import com.niemczuk.passwordwallet.entity.AppLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppLoginRepository extends JpaRepository<AppLogin, Long> {
}

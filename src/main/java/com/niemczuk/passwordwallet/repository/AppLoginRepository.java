package com.niemczuk.passwordwallet.repository;

import com.niemczuk.passwordwallet.dto.AppLoginReadDto;
import com.niemczuk.passwordwallet.entity.AppLogin;
import com.niemczuk.passwordwallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppLoginRepository extends JpaRepository<AppLogin, Long> {

    @Query(value = "SELECT new com.niemczuk.passwordwallet.dto.AppLoginReadDto(" +
            "al.loginTime, " +
            "al.loginResult, " +
            "al.ipAddress " +
            ") " +
            "FROM AppLogin al " +
            "WHERE al.user=?1 " +
            "ORDER BY al.loginTime DESC")
    List<AppLoginReadDto> findAllByUserCustom(User user);
}

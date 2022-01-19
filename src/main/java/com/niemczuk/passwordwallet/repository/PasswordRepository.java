package com.niemczuk.passwordwallet.repository;

import com.niemczuk.passwordwallet.dto.PasswordPackageDto;
import com.niemczuk.passwordwallet.entity.Password;
import com.niemczuk.passwordwallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PasswordRepository extends JpaRepository<Password, UUID> {

    @Query(value = "SELECT new com.niemczuk.passwordwallet.dto.PasswordPackageDto(" +
            "p.id, " +
            "p.login, " +
            "p.password, " +
            "p.webAddress, " +
            "p.description" +
            ") " +
            "FROM Password p " +
            "WHERE p.user=?1")
    List<PasswordPackageDto> findAllByUserCustom(User user);

    List<Password> findAllByUser(User user);
}

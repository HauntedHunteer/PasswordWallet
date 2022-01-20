package com.niemczuk.passwordwallet.repository;

import com.niemczuk.passwordwallet.dto.SharedPasswordForUserDto;
import com.niemczuk.passwordwallet.dto.SharedPasswordOwnerDto;
import com.niemczuk.passwordwallet.entity.SharedPassword;
import com.niemczuk.passwordwallet.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SharedPasswordRepository extends JpaRepository<SharedPassword, UUID> {

    @Query(value = "SELECT new com.niemczuk.passwordwallet.dto.SharedPasswordOwnerDto( " +
            "sp.id, " +
            "sp.login, " +
            "sp.ownerPassword, " +
            "sp.webAddress, " +
            "sp.description," +
            "sp.sharedTo" +
            ")" +
            "FROM SharedPassword sp " +
            "WHERE sp.owner=?1")
    List<SharedPasswordOwnerDto> findSharedPasswordByOwner(User owner);

    @Query(value = "SELECT new com.niemczuk.passwordwallet.dto.SharedPasswordForUserDto( " +
            "sp.login, " +
            "sp.sharedPassword, " +
            "sp.webAddress, " +
            "sp.description," +
            "sp.owner" +
            ")" +
            "FROM SharedPassword sp " +
            "WHERE sp.sharedTo=?1")
    List<SharedPasswordForUserDto> findSharedPasswordBySharedTo(User user);
}

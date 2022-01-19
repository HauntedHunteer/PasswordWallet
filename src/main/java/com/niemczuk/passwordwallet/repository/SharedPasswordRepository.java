package com.niemczuk.passwordwallet.repository;

import com.niemczuk.passwordwallet.entity.SharedPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SharedPasswordRepository extends JpaRepository<SharedPassword, UUID> {

}

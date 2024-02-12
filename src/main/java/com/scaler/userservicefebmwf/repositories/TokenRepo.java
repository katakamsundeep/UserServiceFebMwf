package com.scaler.userservicefebmwf.repositories;

import com.scaler.userservicefebmwf.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenRepo extends JpaRepository<Token , Long> {

    Token save(Token token);

    Optional<Token> findByValueAndDeletedEquals(String value, boolean isDeleted);

    Optional<Token> findByValueAndDeletedEqualsAndExpiryAtGreaterThan(String value, boolean isDeleted, Date expiryGreaterThan);
}

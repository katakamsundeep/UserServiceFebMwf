package com.scaler.userservicefebmwf.repositories;

import com.scaler.userservicefebmwf.models.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User save(User user);

    Optional<User> findByEmail(String email);
}

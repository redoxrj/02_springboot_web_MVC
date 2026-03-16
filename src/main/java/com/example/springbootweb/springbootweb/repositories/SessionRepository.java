package com.example.springbootweb.springbootweb.repositories;

import com.example.springbootweb.springbootweb.entities.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<SessionEntity,Long> {
     List<SessionEntity> findByUserId(Long userId); //Optional<>  is meant for single value not for List(collections) {empty is no record found}.
    Optional<SessionEntity> findByRefreshToken(String refreshToken);

}

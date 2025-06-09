package com.erasmus.goats.Planning_Poker.repository.jpa;

import com.erasmus.goats.Planning_Poker.repository.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionJpaRepository extends JpaRepository<Session, String> {
    Optional<Session> findBySessionId(String sessionId);
}

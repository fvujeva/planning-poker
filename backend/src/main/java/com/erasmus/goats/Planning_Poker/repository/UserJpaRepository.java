package com.erasmus.goats.Planning_Poker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findBySession_SessionId(String sessionSessionId);
}

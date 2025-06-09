package com.erasmus.goats.Planning_Poker.repository.jpa;

import com.erasmus.goats.Planning_Poker.repository.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserJpaRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findBySession_SessionId(String sessionSessionId);
}

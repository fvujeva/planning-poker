package com.erasmus.goats.Planning_Poker.repository;

import java.util.List;

public interface UserRepository {
    List<AppUser> findBySessionId(String sessionId);
    void save(AppUser user);
    AppUser findById(Long userId);
}

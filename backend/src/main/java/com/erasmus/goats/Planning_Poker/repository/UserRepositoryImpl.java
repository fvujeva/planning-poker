package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.repository.entity.AppUser;
import com.erasmus.goats.Planning_Poker.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public List<AppUser> findBySessionId(String sessionId) {
        return userJpaRepository.findBySession_SessionId(sessionId);
    }

    @Override
    public void save(AppUser user) {
        userJpaRepository.save(user);
    }

    @Override
    public AppUser findById(Long userId) {
        return userJpaRepository.findById(userId).orElse(null);
    }
}

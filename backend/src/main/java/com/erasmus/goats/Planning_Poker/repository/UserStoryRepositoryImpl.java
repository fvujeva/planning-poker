package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.repository.entity.UserStory;
import com.erasmus.goats.Planning_Poker.repository.jpa.UserStoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserStoryRepositoryImpl implements UserStoryRepository {

    private final UserStoryJpaRepository userStoryJpaRepository;

    @Override
    public void save(UserStory userStory) {
        userStoryJpaRepository.save(userStory);
    }

    @Override
    public UserStory findById(Long id) {
        return userStoryJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        userStoryJpaRepository.deleteById(id);
    }

    @Override
    public List<UserStory> findAllBySessionId(String sessionId) {
        return userStoryJpaRepository.findAllBySession_SessionId(sessionId).orElse(null);
    }
}

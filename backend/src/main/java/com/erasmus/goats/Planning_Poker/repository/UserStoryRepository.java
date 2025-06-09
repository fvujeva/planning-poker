package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.repository.entity.UserStory;

import java.util.List;

public interface UserStoryRepository {

    void save(UserStory userStory);

    UserStory findById(Long id);

    void deleteById(Long id);

    List<UserStory> findAllBySessionId(String sessionId);
}

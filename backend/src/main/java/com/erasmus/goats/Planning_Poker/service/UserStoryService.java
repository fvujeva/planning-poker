package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.repository.entity.UserStory;

import java.util.List;

public interface UserStoryService {
    List<UserStory> findAllBySessionId(String sessionId);
    void save(UserStory userStory);
    UserStory findById(Long id);
    void deleteById(Long id);
}

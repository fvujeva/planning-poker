package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.repository.entity.UserStory;
import com.erasmus.goats.Planning_Poker.repository.UserStoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserStoryServiceImpl implements UserStoryService {

    private final UserStoryRepository userStoryRepository;

    @Override
    public List<UserStory> findAllBySessionId(String sessionId) {
        return userStoryRepository.findAllBySessionId(sessionId);
    }

    @Override
    public void save(UserStory userStory) {
        userStoryRepository.save(userStory);
    }

    @Override
    public UserStory findById(Long id) {
        return userStoryRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        userStoryRepository.deleteById(id);
    }
}

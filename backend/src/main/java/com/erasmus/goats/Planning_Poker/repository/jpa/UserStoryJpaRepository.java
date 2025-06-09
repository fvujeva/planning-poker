package com.erasmus.goats.Planning_Poker.repository.jpa;

import com.erasmus.goats.Planning_Poker.repository.entity.UserStory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserStoryJpaRepository extends JpaRepository<UserStory, Long> {
    Optional<List<UserStory>> findAllBySession_SessionId(String sessionId);
}

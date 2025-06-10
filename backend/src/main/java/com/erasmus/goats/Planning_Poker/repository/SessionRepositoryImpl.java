package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.repository.entity.Session;
import com.erasmus.goats.Planning_Poker.repository.entity.Vote;
import com.erasmus.goats.Planning_Poker.repository.jpa.SessionJpaRepository;
import com.erasmus.goats.Planning_Poker.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SessionRepositoryImpl implements SessionRepository {

    private final SessionJpaRepository sessionJpaRepository;

    private final Map<Integer, String> userMap = new HashMap<>();
    private final Map<Integer, Vote> voteMap = new HashMap<>();
    private final UserJpaRepository userJpaRepository;
    private Integer adminId = null;

    @Override
    public Optional<Session> findSessionById(String sessionId) {
        return sessionJpaRepository.findById(sessionId);
    }

    @Override
    public void save(Session session) {
        sessionJpaRepository.save(session);
    }

    public boolean isAdmin(int userId) {
        return adminId != null && adminId == userId;
    }

    public void saveVote(Long userId, int voteValue) {
        var user = userJpaRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setVote(voteValue);
        userJpaRepository.save(user);
    }

    @Override
    public Optional<Session> findSessionBySessionId(String sessionId) {
        return sessionJpaRepository.findBySessionId(sessionId);
    }

    public void deleteVotes() {
        voteMap.clear();
    }
}

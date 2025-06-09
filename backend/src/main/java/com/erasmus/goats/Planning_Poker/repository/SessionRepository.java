package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.repository.entity.Session;

import java.util.Optional;

public interface SessionRepository {

    Optional<Session> findSessionById(String id);

    Optional<Session> findSessionBySessionId(String sessionId);

    void save(Session session);

    boolean isAdmin(int userId);

    void saveVote(Long userId, int vote);

    void deleteVotes();
}

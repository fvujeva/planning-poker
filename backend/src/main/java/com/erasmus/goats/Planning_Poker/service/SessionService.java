package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.controller.dto.*;
import com.erasmus.goats.Planning_Poker.repository.entity.Session;

import java.util.List;
import java.util.Map;

public interface SessionService {

    Session findBySessionId(String sessionId);

    Map<String, Object> joinSession(JoinSessionRequestDto request);

    boolean isAdmin(int userId);

    void submitVote(int userId, int vote);

    ResultResponseDto calculateResults(Long userId);

    void clearVotes();

    CreateSessionResponseDto createSession(CreateSessionRequestDto request);

    List<SessionHistoryDto> getUserSessionHistory(Long userId);
}

package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.controller.dto.CreateSessionRequestDto;
import com.erasmus.goats.Planning_Poker.controller.dto.CreateSessionResponseDto;
import com.erasmus.goats.Planning_Poker.controller.dto.JoinSessionRequestDto;
import com.erasmus.goats.Planning_Poker.controller.dto.ResultResponseDto;

import java.util.Map;

public interface SessionService
{
    Map<String, Object> joinSession(JoinSessionRequestDto request);

    boolean isAdmin(int userId);

    void submitVote(int userId, int vote);

    ResultResponseDto calculateResults(Long userId);

    void clearVotes();

    CreateSessionResponseDto createSession(CreateSessionRequestDto request);
}

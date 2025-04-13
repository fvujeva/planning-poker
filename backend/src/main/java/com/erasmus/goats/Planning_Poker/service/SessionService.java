package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.controller.dto.ResultResponseDto;

public interface SessionService
{
    int addUser(String username, boolean isAdmin);

    boolean isAdmin(int userId);

    void submitVote(int userId, int vote);

    ResultResponseDto calculateResults(int userId);

    void clearVotes();
}

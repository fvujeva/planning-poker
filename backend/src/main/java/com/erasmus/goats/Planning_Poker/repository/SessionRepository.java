package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.model.Vote;

import java.util.Map;

public interface SessionRepository {

    int addUser(String username, boolean isAdmin);

    boolean isAdmin(int userId);

    void saveVote(int userId, int vote);

    void deleteVotes();

    Map<Integer, Vote> getVotes();

    Map<Integer, String> getUserMap();
}

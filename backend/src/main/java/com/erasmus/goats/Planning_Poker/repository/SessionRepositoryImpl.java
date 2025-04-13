package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.model.Vote;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SessionRepositoryImpl implements SessionRepository {

    private final Map<Integer, String> userMap = new HashMap<>();
    private final Map<Integer, Vote> voteMap = new HashMap<>();
    private Integer adminId = null;
    private int userIdCounter = 1;

    public int addUser(String username, boolean isAdmin) {
        int userId = userIdCounter++;
        userMap.put(userId, username);
        if (isAdmin) {
            adminId = userId;
        }
        return userId;
    }

    public boolean isAdmin(int userId) {
        return adminId != null && adminId == userId;
    }

    public void saveVote(int userId, int voteValue) {
        String username = userMap.get(userId);
        if (username != null) {
            voteMap.put(userId, new Vote(userId, username, voteValue));
        }
    }

    public Map<Integer, Vote> getVotes() {
        return voteMap;
    }

    @Override
    public Map<Integer, String> getUserMap() {
        return userMap;
    }

    public void deleteVotes() {
        voteMap.clear();
    }
}

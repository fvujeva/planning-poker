package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.controller.dto.ResultResponseDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SessionService {

    private final Map<Integer, String> userMap = new HashMap<>();;
    private final Map<Integer, Integer> votes = new HashMap<>();

    private Integer adminId = null;
    private int userIdCounter = 1;

    public int addUser(String username, boolean isAdmin) {
        int userId = userIdCounter++;
        userMap.put(userId, username); // store the username
        if (isAdmin) {
            adminId = userId;
        }
        return userId;
    }


    public boolean isAdmin(int userId) {
        return adminId != null && adminId == userId;
    }

    public void submitVote(int userId, int vote) {
        votes.put(userId, vote);
    }

    public ResultResponseDto calculateResults(int userId) {
        List<Map<String, Object>> voteList = votes.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> voteInfo = new HashMap<>();
                    voteInfo.put("userId", entry.getKey());
                    voteInfo.put("username", userMap.get(entry.getKey()));
                    voteInfo.put("vote", entry.getValue());
                    return voteInfo;
                })
                .toList();

        double sum = votes.values().stream().mapToInt(Integer::intValue).sum();
        double average = !votes.isEmpty() ? sum / votes.size() : 0;

        return new ResultResponseDto(voteList, Math.round(average * 100.0) / 100.0);
    }




    public void clearVotes() {
        votes.clear();
    }
}


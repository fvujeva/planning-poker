package com.erasmus.goats.Planning_Poker.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SessionService {

    // Store votes in a map with userId as key and vote as value
    private final Map<Integer, Integer> votes = new HashMap<>();

    // Store userId and the user who is an admin
    private Integer adminId = null;
    private int userIdCounter = 1;  // To generate unique user IDs

    // Add user to session and return the userId (assign admin if needed)
    public int addUser(String username, boolean isAdmin) {
        int userId = userIdCounter++;
        if (isAdmin) {
            adminId = userId;  // First user to join is the admin
        }
        return userId;
    }

    // Check if the user is the admin
    public boolean isAdmin(int userId) {
        return adminId != null && adminId == userId;
    }

    // Submit a vote for a specific user
    public void submitVote(int userId, int vote) {
        votes.put(userId, vote);
    }

    // Get results - only accessible by the admin
    public Map<String, Object> calculateResults(int userId) {
        if (!isAdmin(userId)) {
            return Map.of("error", "Only the admin can reveal results.");
        }

        // Create a list of votes with userId and vote
        List<Map<String, Integer>> voteList = votes.entrySet().stream()
                .map(entry -> Map.of("userId", entry.getKey(), "vote", entry.getValue()))
                .toList();

        // Calculate the average of votes
        double sum = votes.values().stream().mapToInt(Integer::intValue).sum();
        double average = !votes.isEmpty() ? sum / votes.size() : 0;

        return Map.of(
                "votes", voteList,
                "average", Math.round(average * 100.0) / 100.0
        );
    }
}


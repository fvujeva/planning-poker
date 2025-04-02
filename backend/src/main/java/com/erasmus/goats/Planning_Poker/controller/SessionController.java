package com.erasmus.goats.Planning_Poker.controller;

import com.erasmus.goats.Planning_Poker.service.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/session")
public class SessionController {

    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // Endpoint for user to join the session (can specify if user is admin)
    @PostMapping("/join")
    public ResponseEntity<Map<String, Object>> joinSession(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("username");
        boolean isAdmin = (boolean) request.getOrDefault("isAdmin", false);

        // Create a user and assign an ID
        int userId = sessionService.addUser(username, isAdmin);
        log.info("username is {} and userid is {}", username, userId);

        return ResponseEntity.ok(Map.of(
                "message", "User joined successfully",
                "userId", userId,
                "isAdmin", isAdmin
        ));
    }

    // Endpoint to submit a vote
    @PostMapping("/vote")
    public ResponseEntity<Map<String, String>> submitVote(@RequestBody Map<String, Object> request) {
        int userId = (int) request.get("userId");
        int vote = (int) request.get("vote");

        // Submit the vote
        sessionService.submitVote(userId, vote);
        log.info("vote is {} and userid is {}", vote, userId);
        return ResponseEntity.ok(Map.of("message", "Vote submitted successfully"));
    }

    // Endpoint for admin to view the results (votes and average)
    @GetMapping("/results/{userId}")
    public ResponseEntity<Map<String, Object>> getResults(@PathVariable int userId) {
        Map<String, Object> results = sessionService.calculateResults(userId);
        log.info("results is {}", results);
        if (results.containsKey("error")) {
            return ResponseEntity.status(403).body(results); // Forbidden if not admin
        }

        return ResponseEntity.ok(results);
    }
}


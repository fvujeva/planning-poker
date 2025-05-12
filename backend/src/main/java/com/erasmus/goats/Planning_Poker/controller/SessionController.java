package com.erasmus.goats.Planning_Poker.controller;

import com.erasmus.goats.Planning_Poker.controller.dto.*;
import com.erasmus.goats.Planning_Poker.service.SessionService;
import com.erasmus.goats.Planning_Poker.service.SessionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/join")
    public ResponseEntity<?> joinSession(@RequestBody JoinSessionRequestDto request) {
        return ResponseEntity.ok(sessionService.joinSession(request));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestBody CreateSessionRequestDto request) {
        return ResponseEntity.ok(sessionService.createSession(request));
    }


    @PostMapping("/vote")
    public ResponseEntity<GenericResponseDto> submitVote(@RequestBody VoteRequestDto request) {
        sessionService.submitVote(request.getUserId(), request.getVote());
        log.info("vote is {} and userid is {}", request.getVote(), request.getUserId());
        return ResponseEntity.ok(new GenericResponseDto("Vote submitted successfully"));
    }

    @GetMapping("/results/{userId}")
    public ResponseEntity<?> getResults(@PathVariable Long userId) {
        ResultResponseDto result = sessionService.calculateResults(userId);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/reset")
    public ResponseEntity<GenericResponseDto> resetVotes() {
        sessionService.clearVotes();
        log.info("Votes have been cleared.");
        return ResponseEntity.ok(new GenericResponseDto("Votes cleared successfully"));
    }
}

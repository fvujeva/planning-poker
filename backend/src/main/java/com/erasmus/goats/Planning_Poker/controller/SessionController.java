package com.erasmus.goats.Planning_Poker.controller;

import com.erasmus.goats.Planning_Poker.controller.dto.*;
import com.erasmus.goats.Planning_Poker.service.SessionService;
import com.erasmus.goats.Planning_Poker.service.SessionServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService sessionService;
    private final SimpMessagingTemplate messagingTemplate;

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
    public ResponseEntity<?> getResults(@PathVariable Long userId, @RequestParam String sessionId) {
        ResultResponseDto result = sessionService.calculateResults(userId);
        String destination = "/topic/results/" + sessionId;
        messagingTemplate.convertAndSend(destination, result);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getSessionHistory(@PathVariable Long userId) {
        List<SessionHistoryDto> history = sessionService.getUserSessionHistory(userId);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/reset")
    public ResponseEntity<GenericResponseDto> resetVotes(@RequestParam String sessionId) {
        sessionService.clearVotes();
        log.info("Votes have been cleared.");
        String destination = "/topic/reset/" + sessionId;
        messagingTemplate.convertAndSend(destination, true);
        return ResponseEntity.ok(new GenericResponseDto("Votes cleared successfully"));
    }
}

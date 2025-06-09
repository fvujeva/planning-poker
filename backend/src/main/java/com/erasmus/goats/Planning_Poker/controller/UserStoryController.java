package com.erasmus.goats.Planning_Poker.controller;

import com.erasmus.goats.Planning_Poker.controller.dto.UserStoryDto;
import com.erasmus.goats.Planning_Poker.repository.entity.Session;
import com.erasmus.goats.Planning_Poker.repository.entity.UserStory;
import com.erasmus.goats.Planning_Poker.service.SessionService;
import com.erasmus.goats.Planning_Poker.service.UserStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userstories")
public class UserStoryController {

    private final UserStoryService userStoryService;
    private final SessionService sessionService;

    public UserStoryController(UserStoryService userStoryService, SessionService sessionService) {
        this.userStoryService = userStoryService;
        this.sessionService = sessionService;
    }

    @GetMapping("/session/{sessionId}")
    public List<UserStory> getAll(@PathVariable String sessionId) {
        return userStoryService.findAllBySessionId(sessionId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStory> getById(@PathVariable Long id) {
        var userStory = userStoryService.findById(id);

        return ResponseEntity.ok(userStory);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserStoryDto userStoryDto) {
        var session = sessionService.findBySessionId(userStoryDto.getSessionId());

        if (session == null) {
            return ResponseEntity.badRequest().body("Invalid session ID");
        }

        UserStory userStory = new UserStory();
        userStory.setTitle(userStoryDto.getTitle());
        userStory.setDescription(userStoryDto.getDescription());
        userStory.setSession(session);

        userStoryService.save(userStory);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStory> update(@PathVariable Long id, @RequestBody UserStory userStory) {
        var oldUserStory = userStoryService.findById(id);
        if (oldUserStory != null) {
            userStory.setId(id);
            userStoryService.save(userStory);
            return ResponseEntity.ok(userStory);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (userStoryService.findById(id) != null) {
            userStoryService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
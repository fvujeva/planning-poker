package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.controller.dto.CreateSessionRequestDto;
import com.erasmus.goats.Planning_Poker.controller.dto.CreateSessionResponseDto;
import com.erasmus.goats.Planning_Poker.controller.dto.JoinSessionRequestDto;
import com.erasmus.goats.Planning_Poker.controller.dto.ResultResponseDto;
import com.erasmus.goats.Planning_Poker.model.Vote;
import com.erasmus.goats.Planning_Poker.repository.entity.Session;
import com.erasmus.goats.Planning_Poker.repository.SessionRepository;
import com.erasmus.goats.Planning_Poker.repository.entity.AppUser;
import com.erasmus.goats.Planning_Poker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Override
    public Session findBySessionId(String sessionId) {
       return sessionRepository.findSessionBySessionId(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public Map<String, Object> joinSession(JoinSessionRequestDto req) {
        Session session;

        if (req.sessionId() != null && !req.sessionId().isEmpty()) {
            session = sessionRepository.findSessionBySessionId(req.sessionId())
                    .orElseThrow(() -> new RuntimeException("Session not found"));
        } else {
            session = new Session();
            session.setSessionId(UUID.randomUUID().toString());
            sessionRepository.save(session);
        }

        AppUser user = new AppUser();
        user.setUsername(req.username());
        user.setAdmin(req.isAdmin());
        user.setSession(session);
        userRepository.save(user);

        Map<String, Object> res = new HashMap<>();
        res.put("userId", user.getId());
        res.put("username", user.getUsername());
        res.put("isAdmin", user.isAdmin());
        res.put("sessionId", session.getSessionId());

        return res;
    }

    @Override
    @Transactional
    public CreateSessionResponseDto createSession(CreateSessionRequestDto request) {
        var session = new Session();
        session.setSessionId(UUID.randomUUID().toString());
        AppUser user = new AppUser();
        user.setUsername(request.getAdminName());
        user.setAdmin(true);
        user.setSession(session);
        userRepository.save(user);
        session.setUsers(List.of(user));
        sessionRepository.save(session);

        return new CreateSessionResponseDto(session.getSessionId(), user.getId());
    }


    @Override
    public boolean isAdmin(int userId) {
        return sessionRepository.isAdmin(userId);
    }

    @Override
    public void submitVote(int userId, int vote) {
        sessionRepository.saveVote((long) userId, vote);
    }


    @Override
    public ResultResponseDto calculateResults(Long userId) {
        AppUser user = userRepository.findById(userId);

        String sessionId = user.getSession().getSessionId();
        List<AppUser> users = userRepository.findBySessionId(sessionId);

        List<Map<String, Object>> votes = new ArrayList<>();
        double sum = 0;
        int count = 0;

        for (AppUser u : users) {
            if (u.getVote() != 0) {
                sum += u.getVote();
                count++;
            }
            votes.add(Map.of("username", u.getUsername(), "vote", u.getVote()));
        }

        double avg = count > 0 ? sum / count : 0;

        return new ResultResponseDto(votes, avg);
    }


    @Override
    public void clearVotes() {
        sessionRepository.deleteVotes();
    }

    private List<Map<String, Object>> mapVotesToResponseList(Map<Integer, Vote> voteMap) {
        return voteMap.values().stream()
                .map(vote -> {
                    Map<String, Object> voteInfo = new HashMap<>();
                    voteInfo.put("userId", vote.getUserId());
                    voteInfo.put("username", vote.getUsername());
                    voteInfo.put("vote", vote.getValue());
                    return voteInfo;
                })
                .toList();
    }

}

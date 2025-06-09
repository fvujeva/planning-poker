package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.controller.dto.*;
import com.erasmus.goats.Planning_Poker.repository.VoteRepository;
import com.erasmus.goats.Planning_Poker.repository.entity.Session;
import com.erasmus.goats.Planning_Poker.repository.SessionRepository;
import com.erasmus.goats.Planning_Poker.repository.entity.AppUser;
import com.erasmus.goats.Planning_Poker.repository.UserRepository;
import com.erasmus.goats.Planning_Poker.repository.entity.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

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
        Session session = user.getSession();
        List<AppUser> users = userRepository.findBySessionId(session.getSessionId());

        List<Map<String, Object>> votes = new ArrayList<>();
        double sum = 0;
        int count = 0;

        for (AppUser u : users) {
            int voteValue = u.getVote();

            if (voteValue != 0) {
                sum += voteValue;
                count++;
            }

            // Save vote entity
            Vote vote = new Vote();
            vote.setUser(u);
            vote.setSession(session);
            vote.setValue(voteValue);
            vote.setTimestamp(LocalDateTime.now());
            voteRepository.save(vote);

            votes.add(Map.of("username", u.getUsername(), "vote", voteValue));
        }

        double avg = count > 0 ? sum / count : 0;

        session.setActive(false);
        session.setAvg(avg);
        sessionRepository.save(session);

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
                    voteInfo.put("userId", vote.getUser().getId());
                    voteInfo.put("username", vote.getUser().getUsername());
                    voteInfo.put("vote", vote.getValue());
                    return voteInfo;
                })
                .toList();
    }

    public List<SessionHistoryDto> getUserSessionHistory(Long userId) {
        AppUser user = userRepository.findById(userId);
        List<Vote> userVotes = voteRepository.findByUser(user);

        // Group by session
        Map<Session, List<Vote>> grouped = userVotes.stream()
                .collect(Collectors.groupingBy(Vote::getSession));

        List<SessionHistoryDto> history = new ArrayList<>();

        for (Map.Entry<Session, List<Vote>> entry : grouped.entrySet()) {
            Session session = entry.getKey();
            List<Vote> votes = entry.getValue();

            List<VoteDto> voteDtos = votes.stream()
                    .map(v -> new VoteDto(v.getUser().getUsername(), v.getValue()))
                    .collect(Collectors.toList());

            SessionHistoryDto dto = new SessionHistoryDto(session.getSessionId(), session.getAvg(), voteDtos);
            history.add(dto);
        }

        return history;
    }
}

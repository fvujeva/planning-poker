package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.controller.dto.ResultResponseDto;
import com.erasmus.goats.Planning_Poker.model.Vote;
import com.erasmus.goats.Planning_Poker.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    @Override
    public int addUser(String username, boolean isAdmin) {
        return sessionRepository.addUser(username, isAdmin);
    }

    @Override
    public boolean isAdmin(int userId) {
        return sessionRepository.isAdmin(userId);
    }

    @Override
    public void submitVote(int userId, int vote) {
        if (!sessionRepository.getUserMap().containsKey(userId)) {
            throw new IllegalArgumentException("User does not exist: " + userId);
        }
        sessionRepository.saveVote(userId, vote);
    }


    @Override
    public ResultResponseDto calculateResults(int userId) {
        Map<Integer, Vote> voteMap = sessionRepository.getVotes();
        List<Map<String, Object>> voteList = mapVotesToResponseList(voteMap);

        double sum = voteMap.values().stream().mapToInt(Vote::getValue).sum();
        double average = !voteMap.isEmpty() ? sum / voteMap.size() : 0;

        return new ResultResponseDto(voteList, Math.round(average * 100.0) / 100.0);
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

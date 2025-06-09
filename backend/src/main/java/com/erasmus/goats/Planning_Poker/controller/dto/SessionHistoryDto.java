package com.erasmus.goats.Planning_Poker.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionHistoryDto {
    private String sessionId;
    private double average;
    private List<VoteDto> votes;
}

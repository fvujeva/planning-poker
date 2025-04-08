package com.erasmus.goats.Planning_Poker.controller.dto;

import lombok.Data;

@Data
public class VoteRequestDto {
    private int userId;
    private int vote;
}


package com.erasmus.goats.Planning_Poker.controller.dto;

import lombok.Data;

@Data
public class JoinSessionRequestDto {
    private String username;
    private boolean isAdmin;
}


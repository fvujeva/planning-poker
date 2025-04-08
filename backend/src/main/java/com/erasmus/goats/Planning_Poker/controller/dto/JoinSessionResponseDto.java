package com.erasmus.goats.Planning_Poker.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinSessionResponseDto {
    private String message;
    private int userId;
    private boolean isAdmin;
}


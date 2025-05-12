package com.erasmus.goats.Planning_Poker.controller.dto;

import lombok.Data;

public record JoinSessionRequestDto(String username, boolean isAdmin, String sessionId){
}


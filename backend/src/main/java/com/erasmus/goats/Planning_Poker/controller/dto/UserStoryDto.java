package com.erasmus.goats.Planning_Poker.controller.dto;

import lombok.Data;

@Data
public class UserStoryDto {
    private String title;
    private String description;
    private String sessionId;
}

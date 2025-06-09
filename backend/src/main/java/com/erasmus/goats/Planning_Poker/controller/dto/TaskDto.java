package com.erasmus.goats.Planning_Poker.controller.dto;

import lombok.Data;

@Data
public class TaskDto {
    private String title;
    private String description;
    private Long userStoryId;
}

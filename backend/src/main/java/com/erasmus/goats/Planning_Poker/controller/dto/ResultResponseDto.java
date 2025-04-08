package com.erasmus.goats.Planning_Poker.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ResultResponseDto {
    private List<Map<String, Object>> votes;
    private double average;

}



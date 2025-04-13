package com.erasmus.goats.Planning_Poker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vote {
    private int userId;
    private String username;
    private int value;
}


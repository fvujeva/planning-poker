// CreateSessionResponseDto.java
package com.erasmus.goats.Planning_Poker.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSessionResponseDto {
    private String sessionId;
    private Long adminId;
}

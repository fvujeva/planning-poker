package com.erasmus.goats.Planning_Poker.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int value;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Session session;

    private LocalDateTime timestamp;
}

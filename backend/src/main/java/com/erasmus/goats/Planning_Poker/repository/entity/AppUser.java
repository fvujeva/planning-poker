package com.erasmus.goats.Planning_Poker.repository.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private boolean isAdmin;
    private int vote;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;

}


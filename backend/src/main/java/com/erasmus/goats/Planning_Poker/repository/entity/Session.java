package com.erasmus.goats.Planning_Poker.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String sessionId;

    @Column
    private boolean active = true;

    @Column
    private double avg;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<AppUser> users = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStory> userStories = new ArrayList<>();
}
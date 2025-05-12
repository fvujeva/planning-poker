package com.erasmus.goats.Planning_Poker.repository;

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

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<AppUser> users = new ArrayList<>();

}


package com.erasmus.goats.Planning_Poker.repository.entity;// src/main/java/com/erasmus/goats/Planning_Poker/model/Task.java
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @ManyToOne
    @JoinColumn(name = "user_story_id")
    @JsonBackReference
    private UserStory userStory;
}
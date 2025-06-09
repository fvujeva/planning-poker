package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.repository.entity.Task;

import java.util.List;

public interface TaskRepository {
    void save(Task task);
    Task findById(Long id);
    void deleteById(Long id);
    List<Task> findAllByUserStory(Long userStoryId);
}

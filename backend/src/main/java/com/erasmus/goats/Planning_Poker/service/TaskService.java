package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.repository.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> findAllByUserStoryId(Long userStoryId);
    Task findById(Long id);
    void save(Task task);
    void deleteById(Long id);
}

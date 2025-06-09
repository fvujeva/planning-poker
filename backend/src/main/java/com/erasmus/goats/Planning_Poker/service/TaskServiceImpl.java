package com.erasmus.goats.Planning_Poker.service;

import com.erasmus.goats.Planning_Poker.repository.entity.Task;
import com.erasmus.goats.Planning_Poker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    public List<Task> findAllByUserStoryId(Long userStoryId) {
        return taskRepository.findAllByUserStory(userStoryId);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id);
    }

    public void save(Task task) {
        taskRepository.save(task);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }
}
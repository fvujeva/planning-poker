package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.repository.entity.Task;
import com.erasmus.goats.Planning_Poker.repository.jpa.TaskJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryimpl implements TaskRepository{

    private final TaskJpaRepository taskJpaRepository;

    @Override
    public void save(Task task) {
        taskJpaRepository.save(task);
    }

    @Override
    public Task findById(Long id) {
        return taskJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        taskJpaRepository.deleteById(id);
    }

    @Override
    public List<Task> findAllByUserStory(Long userStoryId) {
        return taskJpaRepository.findAllByUserStory_Id(userStoryId).orElse(null);
    }
}

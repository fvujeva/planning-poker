package com.erasmus.goats.Planning_Poker.repository.jpa;

import com.erasmus.goats.Planning_Poker.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {
    Optional<List<Task>> findAllByUserStory_Id(Long userStoryId);
}

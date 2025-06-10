package com.erasmus.goats.Planning_Poker.repository;

import com.erasmus.goats.Planning_Poker.repository.entity.Vote;
import com.erasmus.goats.Planning_Poker.repository.entity.Session;
import com.erasmus.goats.Planning_Poker.repository.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findBySession(Session session);
    List<Vote> findByUser(AppUser user);
}

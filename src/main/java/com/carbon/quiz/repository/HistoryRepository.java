package com.carbon.quiz.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carbon.quiz.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findAllByUserIdOrderByTimestampDesc(Long userId);
    Optional<History> findFirstByUserIdAndAttemptId(Long userId, String attemptId);
    void deleteAllByUserId(Long userId);
}

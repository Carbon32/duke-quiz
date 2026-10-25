package com.carbon.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carbon.quiz.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findAllByUserId(Long userId);
}

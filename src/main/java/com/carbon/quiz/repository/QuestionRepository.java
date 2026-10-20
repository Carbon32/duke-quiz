package com.carbon.quiz.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carbon.quiz.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(String category);
    boolean existsByQuestionText(String questionText);
}
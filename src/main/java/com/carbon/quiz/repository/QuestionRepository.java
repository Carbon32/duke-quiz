package com.carbon.quiz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.carbon.quiz.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByCategory(String category);
     @Query("SELECT DISTINCT q.category FROM Question q")
    List<String> getAllCategories();
    boolean existsByQuestionText(String questionText);
}
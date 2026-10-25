package com.carbon.quiz.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carbon.quiz.dto.QuestionsRequest;
import com.carbon.quiz.model.Question;
import com.carbon.quiz.service.QuestionService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuestionService questionService;

    public QuizController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/questions")
    public List<Question> getQuestionsByCategory(@RequestBody @Valid QuestionsRequest request) {
        List<Question> questions = questionService.getQuestionByCategory("Science");
        return questions;
    }

    @GetMapping("/categories")
    public List<String> getAvailalbeCategories() {
        return questionService.getAllCategories();
    }
    
}

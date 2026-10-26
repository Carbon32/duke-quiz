package com.carbon.quiz.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.carbon.quiz.dto.QuestionsRequest;
import com.carbon.quiz.model.Question;
import com.carbon.quiz.service.QuestionService;
import com.carbon.quiz.service.HistoryService;
import com.carbon.quiz.model.History;
import com.carbon.quiz.dto.HistoryRequest;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuestionService questionService;
    private final HistoryService historyService;

    public QuizController(QuestionService questionService, HistoryService historyService) {
        this.questionService = questionService;
        this.historyService = historyService;
    }

    @PostMapping("/questions")
    // Yes I'm old fashioned, I still prefer adding an X in front of non-standard headers
    public List<Question> getQuestionsByCategory(@RequestBody @Valid QuestionsRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        if (!userId.equals(request.getUserId())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "User ID does not match request");
        }
        return questionService.getQuestionByCategory(request.getCategory());
    }

    @GetMapping("/categories")
    public List<String> getAvailalbeCategories(@RequestHeader("X-User-Id") Long userId) {
        return questionService.getAllCategories();
    }

    @GetMapping("/history")
    public List<History> getHistory(@RequestHeader("X-User-Id") Long userId) {
        return historyService.getHistoryEntriesByUserId(userId);
    }

    @PutMapping("/history")
    public History saveHistory(@RequestBody @Valid HistoryRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        if (!userId.equals(request.getUserId())) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.FORBIDDEN, "User ID does not match request");
        }
        return historyService.saveAttempt(request);
    }

    @DeleteMapping("/history")
    public void clearHistory(@RequestHeader("X-User-Id") Long userId) {
        historyService.clearHistory(userId);
    }
    
}

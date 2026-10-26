package com.carbon.quiz.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HistoryRequest {
    @JsonProperty("user_id")
    @Positive(message = "User ID must be a positive number")
    private Long userId;

    @JsonProperty("attempt_id")
    @NotBlank(message = "You must provide an attempt ID")
    private String attemptId;

    @JsonProperty("category")
    @NotBlank(message = "Category cannot be empty")
    @Size(min = 2, max = 32, message = "Keep the category short")
    private String category;

    @JsonProperty("total_questions")
    @Positive(message = "Total questions must be a positive number")
    private int totalQuestions;

    @JsonProperty("correct_questions")
    @PositiveOrZero(message = "Correct questions must be a positive number or a zero")
    private int correctQuestions;

    @JsonProperty("wrong_questions")
    @PositiveOrZero(message = "Wrong questions must be a positive number or a zero")
    private int wrongQuestions;
}

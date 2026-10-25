package com.carbon.quiz.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuestionsRequest {
    @JsonProperty("user_id")
    @Positive(message = "User ID must be a positive number")
    private int userId;

    @JsonProperty("category")
    @NotBlank(message = "Category cannot be empty")
    @Size(min = 2, max = 32, message = "Keep the category short")
    private String category;
}

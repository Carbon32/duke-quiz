package com.carbon.quiz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity 
@Table (name = "history")
@Data 
public class History {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @Column(nullable = false)
    public Long userId;
    
    @Column(nullable = false)
    public String category;

    @Column(nullable = false)
    public int totalQuestions;

    @Column(nullable = false)
    public int correctQuestions;


}

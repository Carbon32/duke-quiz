package com.carbon.quiz.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import java.time.Instant;
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

    @Column(nullable = false)
    public int wrongQuestions;

    @Column(nullable = false)
    public String attemptId;

    @Column(nullable = false)
    // Better than the other stuff
    public Instant timestamp;

    @PrePersist
    void setTimestampIfMissing() {
        if (timestamp == null) timestamp = Instant.now();
    }


}

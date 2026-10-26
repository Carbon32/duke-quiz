package com.carbon.quiz.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.carbon.quiz.model.History;
import com.carbon.quiz.repository.HistoryRepository;
import com.carbon.quiz.dto.HistoryRequest;
import org.springframework.transaction.annotation.Transactional;

@Service 
public class HistoryService {
    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public List<History> getAllHistory() {
        return historyRepository.findAll();
    }

    public History getHistoryById(Long id) {
        return historyRepository.findById(id).orElseThrow(() -> new RuntimeException("The history entry is not found, id: " + id));
    }

    @Transactional
    public History saveAttempt(HistoryRequest request) {
        History history = historyRepository
                .findFirstByUserIdAndAttemptId(request.getUserId(), request.getAttemptId())
                .orElseGet(History::new);
        history.setUserId(request.getUserId());
        history.setAttemptId(request.getAttemptId());
        history.setCategory(request.getCategory());
        history.setTotalQuestions(request.getTotalQuestions());
        history.setCorrectQuestions(request.getCorrectQuestions());
        history.setWrongQuestions(request.getWrongQuestions());
        history.setTimestamp(java.time.Instant.now());
        return historyRepository.save(history);
    }

    @Transactional
    public void clearHistory(Long userId) {
        historyRepository.deleteAllByUserId(userId);
    }

    public List<History> getHistoryEntriesByUserId(Long userId) {
        return historyRepository.findAllByUserIdOrderByTimestampDesc(userId);
    }
}

package com.carbon.quiz.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.carbon.quiz.model.History;
import com.carbon.quiz.repository.HistoryRepository;

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

    History createHistoryEntry(History history) {
        return historyRepository.save(history);
    }

    void deleteHistoryEntry(Long id) {
        History history = getHistoryById(id);
        historyRepository.delete(history);
    }

    List<History> getHistoryEntriesByUserId(Long userId) {
        return historyRepository.findAllByUserId(userId);
    }
}

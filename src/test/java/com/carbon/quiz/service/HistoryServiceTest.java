package com.carbon.quiz.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.carbon.quiz.dto.HistoryRequest;
import com.carbon.quiz.model.History;
import com.carbon.quiz.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HistoryServiceTest {
    private HistoryRepository historyRepository;
    private HistoryService historyService;

    @BeforeEach
    void setUp() {
        historyRepository = mock(HistoryRepository.class);
        historyService = new HistoryService(historyRepository);
    }

    @Test
    void saveAttemptCreatesEntryWhenAttemptIsNew() {
        HistoryRequest request = request(42L, "attempt-1", "Culture", 10, 7, 3);
        when(historyRepository.findFirstByUserIdAndAttemptId(42L, "attempt-1"))
                .thenReturn(Optional.empty());
        when(historyRepository.save(any(History.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        History saved = historyService.saveAttempt(request);

        assertEquals(42L, saved.getUserId());
        assertEquals("attempt-1", saved.getAttemptId());
        assertEquals("Culture", saved.getCategory());
        assertEquals(10, saved.getTotalQuestions());
        assertEquals(7, saved.getCorrectQuestions());
        assertEquals(3, saved.getWrongQuestions());
        assertNotNull(saved.getTimestamp());
        verify(historyRepository).save(any(History.class));
    }

    @Test
    void saveAttemptUpdatesExistingEntryInsteadOfCreatingAnother() {
        History existing = new History();
        existing.setId(9L);
        existing.setTimestamp(Instant.parse("2026-01-01T00:00:00Z"));
        HistoryRequest request = request(42L, "attempt-1", "Other", 5, 4, 1);
        when(historyRepository.findFirstByUserIdAndAttemptId(42L, "attempt-1"))
                .thenReturn(Optional.of(existing));
        when(historyRepository.save(existing)).thenReturn(existing);

        History saved = historyService.saveAttempt(request);

        assertSame(existing, saved);
        assertEquals(9L, saved.getId());
        assertEquals("Other", saved.getCategory());
        assertEquals(5, saved.getTotalQuestions());
        assertEquals(4, saved.getCorrectQuestions());
        assertEquals(1, saved.getWrongQuestions());
        assertTrue(saved.getTimestamp().isAfter(Instant.parse("2026-01-01T00:00:00Z")));
        verify(historyRepository, times(1)).save(existing);
    }

    @Test
    void readsAndClearsOnlyTheRequestedUsersHistory() {
        List<History> entries = List.of(new History());
        when(historyRepository.findAllByUserIdOrderByTimestampDesc(42L)).thenReturn(entries);

        assertSame(entries, historyService.getHistoryEntriesByUserId(42L));
        historyService.clearHistory(42L);

        verify(historyRepository).findAllByUserIdOrderByTimestampDesc(42L);
        verify(historyRepository).deleteAllByUserId(42L);
    }

    private static HistoryRequest request(Long userId, String attemptId, String category,
            int total, int correct, int wrong) {
        HistoryRequest request = new HistoryRequest();
        request.setUserId(userId);
        request.setAttemptId(attemptId);
        request.setCategory(category);
        request.setTotalQuestions(total);
        request.setCorrectQuestions(correct);
        request.setWrongQuestions(wrong);
        return request;
    }
}
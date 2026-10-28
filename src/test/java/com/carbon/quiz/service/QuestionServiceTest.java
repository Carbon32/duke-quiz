package com.carbon.quiz.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.carbon.quiz.model.Question;
import com.carbon.quiz.repository.HistoryRepository;
import com.carbon.quiz.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionServiceTest {
    private QuestionRepository questionRepository;
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        questionRepository = mock(QuestionRepository.class);
        questionService = new QuestionService(questionRepository, mock(HistoryRepository.class));
    }

    @Test
    void getQuestionByCategoryDelegatesToRepository() {
        Question question = question(1L, "Culture", "Question 1", 0);
        when(questionRepository.findByCategory("Culture")).thenReturn(List.of(question));

        List<Question> result = questionService.getQuestionByCategory("Culture");

        assertEquals(List.of(question), result);
        verify(questionRepository).findByCategory("Culture");
    }

    @Test
    void checkQuestionAnswerReturnsWhetherIndexMatchesCorrectAnswer() {
        when(questionRepository.findById(7L)).thenReturn(Optional.of(question(7L, "Culture", "Question", 2)));

        assertTrue(questionService.checkQuestionAnswer(7L, 2));
        assertFalse(questionService.checkQuestionAnswer(7L, 1));
    }

    @Test
    void updateQuestionCopiesEditableFieldsAndSavesExistingQuestion() {
        Question current = question(4L, "Culture", "Old text", 0);
        Question update = question(null, "Other", "New text", 1);
        when(questionRepository.findById(4L)).thenReturn(Optional.of(current));
        when(questionRepository.save(current)).thenReturn(current);

        Question result = questionService.updateQuestion(4L, update);

        assertSame(current, result);
        assertEquals("Other", current.getCategory());
        assertEquals("New text", current.getQuestionText());
        assertEquals(List.of("A", "B", "C"), current.getOptions());
        assertEquals(1, current.getCorrectIndex());
        verify(questionRepository).save(current);
    }

    private static Question question(Long id, String category, String text, int correctIndex) {
        Question question = new Question();
        question.setId(id);
        question.setCategory(category);
        question.setQuestionText(text);
        question.setOptions(List.of("A", "B", "C"));
        question.setCorrectIndex(correctIndex);
        return question;
    }
}
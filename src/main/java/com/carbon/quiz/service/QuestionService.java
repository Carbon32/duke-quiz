package com.carbon.quiz.service;

import com.carbon.quiz.repository.HistoryRepository;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.carbon.quiz.model.Question;
import com.carbon.quiz.repository.QuestionRepository;

@Service 
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository, HistoryRepository historyRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new RuntimeException("The question is not found, id: " + id));
    }

    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, Question updatedQuestion) {
        Question actualQuestion = getQuestionById(id);

        actualQuestion.setCategory(updatedQuestion.getCategory());
        actualQuestion.setQuestionText(updatedQuestion.getQuestionText());
        actualQuestion.setOptions(updatedQuestion.getOptions());
        actualQuestion.setCorrectIndex(updatedQuestion.getCorrectIndex());

        return questionRepository.save(actualQuestion);
    }

    public void deleteQuesiton(Long id) {
        Question question = getQuestionById(id);
        questionRepository.delete(question);
    }

    public List<Question> getQuestionByCategory(String category) {
        return questionRepository.findByCategory(category);
    }

    public List<Question> getRandomQuestionsByCategory(String category, int limit) {
        List<Question> questions = questionRepository.findByCategory(category);
        Collections.shuffle(questions);

        return questions.stream().limit(limit).toList();
    }

    public int getQuestionAnswer(Long id) {
        return getQuestionById(id).getCorrectIndex();
    }

    public boolean checkQuestionAnswer(Long id, int index) {
        return getQuestionAnswer(id) == index;
    }

    public List<String> getAllCategories() {
        return questionRepository.getAllCategories();
    }
}
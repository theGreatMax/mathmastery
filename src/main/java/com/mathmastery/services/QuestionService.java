package com.mathmastery.services;

import com.mathmastery.models.Question;
import com.mathmastery.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<Question> getQuestionsBySubtopicId(Long subtopicId) {
        return questionRepository.findBySubtopicId(subtopicId);
    }
}
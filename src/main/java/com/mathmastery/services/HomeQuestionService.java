package com.mathmastery.services;

import com.mathmastery.models.HomeQuestions;
import com.mathmastery.repositories.HomeQuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeQuestionService {

    private final HomeQuestionRepository questionRepository;

    public HomeQuestionService(HomeQuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<HomeQuestions> getAllQuestions() {
        return this.questionRepository.findAll();
    }
}

package com.mathmastery.controllers;

import com.mathmastery.models.Question;
import com.mathmastery.repositories.QuestionRepository;
import com.mathmastery.services.QuestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // GET /api/questions/subtopic/5
    @GetMapping("/subtopic/{subtopicId}")
    public List<Question> getQuestionsBySubtopic(@PathVariable Long subtopicId) {
        return questionService.getQuestionsBySubtopicId(subtopicId);
    }
}

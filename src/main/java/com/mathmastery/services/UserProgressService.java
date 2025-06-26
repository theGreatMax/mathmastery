package com.mathmastery.services;

import com.mathmastery.dto.UserProgressDTO;
import com.mathmastery.models.Question;
import com.mathmastery.models.User;
import com.mathmastery.models.UserProgress;
import com.mathmastery.repositories.QuestionRepository;
import com.mathmastery.repositories.UserProgressRepository;
import com.mathmastery.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserProgressService {

    private final UserProgressRepository progressRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public UserProgressService(UserProgressRepository progressRepository,
                               UserRepository userRepository,
                               QuestionRepository questionRepository) {
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    public void saveProgress(String username, UserProgressDTO dto) {
      User user = userRepository.findByEmail(username).
                orElseThrow(() -> new RuntimeException("User not found"));;
        Question question = questionRepository.findById(dto.getQuestionId()).orElseThrow();

        UserProgress progress = new UserProgress();
        progress.setUser(user);
        progress.setQuestion(question);
        progress.setCorrect(dto.isCorrect());
        progress.setAttemptedAt(LocalDateTime.now());

        progressRepository.save(progress);
    }
}

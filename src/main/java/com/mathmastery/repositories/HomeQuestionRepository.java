package com.mathmastery.repositories;

import com.mathmastery.models.HomeQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeQuestionRepository extends JpaRepository<HomeQuestions, Long> {
}

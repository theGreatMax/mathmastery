package com.mathmastery.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name ="questions")
@Setter
@Getter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "correct_answer")
    private String correctAnswer;

    @Column(name = "sub_questions", columnDefinition = "TEXT")
    private String subQuestions;

    @Column(name = "solution", columnDefinition = "TEXT")
    private String solution;

    @Column(name ="points")
    private int points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "subtopic_id", nullable = false)
    private Subtopic subtopic;
}

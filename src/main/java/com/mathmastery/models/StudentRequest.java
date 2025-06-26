package com.mathmastery.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StudentRequest {
    @Id
    @GeneratedValue
    private Long id;

    private String topic;
    private String status; // e.g., PENDING, ACCEPTED

    @ManyToOne
    private User student;

    @ManyToOne
    private User tutor;

    // getters and setters
}

package com.mathmastery.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tutor_booking_requests")
public class TutorBookingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The student who made the request
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    // The tutor the request is for
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private User tutor;

    // The chosen time slot
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private TutorSchedule schedule;

    @Column(length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();

    public enum Status {
        PENDING,
        APPROVED,
        REJECTED
    }
}

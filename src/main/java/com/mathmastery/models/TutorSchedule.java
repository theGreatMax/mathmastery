package com.mathmastery.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tutor_schedule")
@AllArgsConstructor
@NoArgsConstructor
public class TutorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dayOfWeek; // e.g., "Monday"
    private String startTime; // or use `LocalTime`
    private String endTime;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private User tutor;

    // Getters and setters
}


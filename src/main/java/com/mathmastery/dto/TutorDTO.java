package com.mathmastery.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathmastery.models.TutorBookingRequest;
import com.mathmastery.models.TutorSchedule;
import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorDTO {

    private Long id;
    private String name;
    private String subject;
    private String bio;
    private String imageUrl;
    private String school;
    private List<ScheduleDTO> schedule;
    private String scheduleJson;
    private boolean alreadyBooked;
    private String bookingStatus;


    public String getStatusBadgeClass() {
        if (bookingStatus == null) return "badge bg-secondary";
        return switch (bookingStatus) {
            case "PENDING" -> "badge bg-warning text-dark";
            case "APPROVED" -> "badge bg-success";
            case "REJECTED" -> "badge bg-danger";
            default -> "badge bg-secondary";
        };
    }


}

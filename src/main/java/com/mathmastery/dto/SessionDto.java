package com.mathmastery.dto;

import com.mathmastery.models.Session;
import lombok.Data;

import java.time.format.DateTimeFormatter;

@Data
public class SessionDto {
    private String subject;
    private String studentName;
    private String dateTime;

    public static SessionDto fromEntity(Session session) {
        SessionDto dto = new SessionDto();
        dto.setSubject(session.getSubject());
        dto.setStudentName(session.getStudent().getFullname());
        dto.setDateTime(session.getDateTime().format(DateTimeFormatter.ofPattern("dd MMM, HH:mm")));
        return dto;
    }
}

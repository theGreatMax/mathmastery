package com.mathmastery.services;

import com.mathmastery.dto.SessionDto;

import java.util.List;

public interface SessionService {
    List<SessionDto> getUpcomingSessionsForTutor(Long tutorId);
}

package com.mathmastery.services;

import com.mathmastery.dto.SessionDto;
import com.mathmastery.models.Session;
import com.mathmastery.repositories.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    public SessionServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<SessionDto> getUpcomingSessionsForTutor(Long tutorId) {
        List<Session> sessions = sessionRepository.findUpcomingByTutorId(tutorId);
        return sessions.stream().map(SessionDto::fromEntity).collect(Collectors.toList());
    }
}

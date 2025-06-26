package com.mathmastery.repositories;

import com.mathmastery.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    @Query("SELECT s FROM Session s WHERE s.tutor.id = :tutorId AND s.dateTime >= CURRENT_TIMESTAMP ORDER BY s.dateTime ASC")
    List<Session> findUpcomingByTutorId(Long tutorId);
}

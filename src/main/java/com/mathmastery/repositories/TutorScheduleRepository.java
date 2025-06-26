package com.mathmastery.repositories;

import com.mathmastery.models.TutorSchedule;
import com.mathmastery.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TutorScheduleRepository extends JpaRepository<TutorSchedule, Long> {
    List<TutorSchedule> findByTutor(User tutor);
    List<TutorSchedule> findByDayOfWeek(String dayOfWeek);
}

package com.mathmastery.repositories;

import com.mathmastery.models.TutorBookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutorBookingRequestRepository extends JpaRepository<TutorBookingRequest, Long> {

    List<TutorBookingRequest> findByTutorIdAndStatus(Long tutorId, TutorBookingRequest.Status status);
    boolean existsByTutorIdAndStudentId(Long tutorID, Long studentId);

    boolean existsByTutorIdAndStudentIdAndStatus(Long tutorId, Long studentId, TutorBookingRequest.Status status);

    Optional<TutorBookingRequest> findFirstByTutorIdAndStudentIdOrderByCreatedAtDesc(Long tutorId, Long studentId);


    List<TutorBookingRequest> findByStudentId(Long studentId);
    Optional<TutorBookingRequest> findByTutorIdAndStudentIdAndStatus(Long tutorId, Long studentId, TutorBookingRequest.Status status);


}
